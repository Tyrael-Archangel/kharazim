package com.tyrael.kharazim.pharmacy.app.service;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.pharmacy.app.converter.InventoryConverter;
import com.tyrael.kharazim.pharmacy.app.domain.Inventory;
import com.tyrael.kharazim.pharmacy.app.domain.InventoryLog;
import com.tyrael.kharazim.pharmacy.app.domain.InventoryOccupy;
import com.tyrael.kharazim.pharmacy.app.mapper.InventoryLogMapper;
import com.tyrael.kharazim.pharmacy.app.mapper.InventoryMapper;
import com.tyrael.kharazim.pharmacy.app.mapper.InventoryOccupyMapper;
import com.tyrael.kharazim.pharmacy.app.vo.inventory.*;
import com.tyrael.kharazim.product.sdk.service.ProductServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryMapper inventoryMapper;
    private final InventoryLogMapper inventoryLogMapper;
    private final InventoryOccupyMapper inventoryOccupyMapper;
    private final InventoryConverter inventoryConverter;

    @DubboReference
    private ProductServiceApi productServiceApi;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InventoryDTO> page(PageInventoryRequest pageRequest) {

        String skuCode = pageRequest.getSkuCode();
        String skuName = pageRequest.getSkuName();
        if (StringUtils.isBlank(skuCode) && StringUtils.isNotBlank(skuName)) {
            pageRequest.setFilterSkuCodes(productServiceApi.filterByName(skuName));
        }
        PageResponse<Inventory> pageData = inventoryMapper.page(pageRequest);
        return PageResponse.success(
                inventoryConverter.inventories(pageData.getData()),
                pageData.getTotalCount());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryDTO> listOfClinic(ListInventoryOfClinicRequest listRequest) {
        List<Inventory> inventories = inventoryMapper.listOfClinic(listRequest);
        return inventoryConverter.inventories(inventories);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InventoryLogVO> pageLog(PageInventoryLogRequest pageRequest) {
        PageResponse<InventoryLog> pageData = inventoryLogMapper.page(pageRequest);
        return PageResponse.success(
                inventoryConverter.inventoryLogs(pageData.getData()),
                pageData.getTotalCount());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InventoryOccupyVO> pageOccupy(PageInventoryOccupyRequest pageRequest) {
        PageResponse<InventoryOccupy> pageData = inventoryOccupyMapper.page(pageRequest);

        List<InventoryOccupyVO> occupyPageData = pageData.getData()
                .stream()
                .map(e -> InventoryOccupyVO.builder()
                        .businessCode(e.getBusinessCode())
                        .skuCode(e.getSkuCode())
                        .quantity(e.getQuantity())
                        .clinicCode(e.getClinicCode())
                        .build())
                .collect(Collectors.toList());
        return PageResponse.success(occupyPageData, pageData.getTotalCount());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void occupy(InventoryOccupyCommand occupyCommand) {

        // 先将业务单据原来的预占释放掉（可能没有预占过）
        this.releaseByBusinessCode(occupyCommand);

        // 然后重新预占
        this.doOccupy(occupyCommand);

    }

    private void releaseByBusinessCode(InventoryOccupyCommand occupyCommand) {

        String businessCode = occupyCommand.getBusinessCode();
        String clinicCode = occupyCommand.getClinicCode();

        List<InventoryOccupy> occupied = inventoryOccupyMapper.findOccupied(businessCode, clinicCode);
        if (occupied.isEmpty()) {
            return;
        }

        List<InventoryOccupy> sortedOccupied = occupied.stream()
                .sorted(Comparator.comparing(InventoryOccupy::getSkuCode))
                .toList();

        for (InventoryOccupy inventoryOccupy : sortedOccupied) {
            String skuCode = inventoryOccupy.getSkuCode();
            Integer quantity = inventoryOccupy.getQuantity();

            int updatedRows = inventoryOccupyMapper.releaseExactly(clinicCode, skuCode, businessCode, quantity);
            BusinessException.assertTrue(updatedRows == 1, "处理库存预占失败");

            inventoryMapper.decreaseOccupy(clinicCode, skuCode, quantity);
            // save occupy log
            saveInventoryChangeLog(occupyCommand, new InventoryChangeCommand.Item(skuCode, quantity));
        }

        occupied = inventoryOccupyMapper.findOccupied(businessCode, clinicCode);
        BusinessException.assertTrue(occupied.isEmpty(), "处理库存预占失败（可能同一个单据存在并发）");
    }

    private void doOccupy(InventoryOccupyCommand occupyCommand) {
        List<InventoryChangeCommand.Item> sortedItems = occupyCommand.getItems()
                .stream()
                .sorted(Comparator.comparing(InventoryChangeCommand.Item::skuCode))
                .toList();
        for (InventoryChangeCommand.Item item : sortedItems) {
            int updatedRow = inventoryMapper.increaseOccupy(
                    occupyCommand.getClinicCode(),
                    item.skuCode(),
                    item.quantity());
            BusinessException.assertTrue(updatedRow > 0, "商品库存不足");

            // save occupy log
            saveInventoryChangeLog(occupyCommand, item);

            // save prescription occupy record
            saveOccupyRecord(occupyCommand, item);
        }
    }

    private void saveOccupyRecord(InventoryOccupyCommand occupyCommand, InventoryChangeCommand.Item item) {
        inventoryOccupyMapper.occupy(
                occupyCommand.getClinicCode(),
                item.skuCode(),
                occupyCommand.getBusinessCode(),
                item.quantity());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inbound(InventoryInboundCommand inboundCommand) {

        List<InventoryChangeCommand.Item> sortedItems = CollectionUtils.safeStream(inboundCommand.getItems())
                .sorted(Comparator.comparing(InventoryChangeCommand.Item::skuCode))
                .toList();

        for (InventoryChangeCommand.Item inboundItem : sortedItems) {
            String skuCode = inboundItem.skuCode();
            Integer quantity = inboundItem.quantity();

            // 增加库存
            int row = inventoryMapper.increaseQuantity(inboundCommand.getClinicCode(), skuCode, quantity);
            if (row <= 0) {
                // 库存数据不存在
                Inventory inventory = new Inventory();
                inventory.setClinicCode(inboundCommand.getClinicCode());
                inventory.setSkuCode(skuCode);
                inventory.setQuantity(quantity);
                inventory.setOccupiedQuantity(0);
                inventoryMapper.insert(inventory);
            }

            // 保存库存日志
            saveInventoryChangeLog(inboundCommand, inboundItem);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void outbound(InventoryOutboundCommand outboundCommand) {

        List<InventoryChangeCommand.Item> sortedItems = CollectionUtils.safeStream(outboundCommand.getItems())
                .sorted(Comparator.comparing(InventoryChangeCommand.Item::skuCode))
                .toList();

        for (InventoryChangeCommand.Item outboundItem : sortedItems) {
            // 扣减库存和预占
            inventoryMapper.decreaseQuantityByOccupy(
                    outboundCommand.getClinicCode(),
                    outboundItem.skuCode(),
                    outboundItem.quantity());

            // 保存库存日志
            saveInventoryChangeLog(outboundCommand, outboundItem);

            // save release occupy record
            saveReleaseOccupyRecord(outboundCommand, outboundItem);
        }
    }

    private void saveReleaseOccupyRecord(InventoryOutboundCommand outboundCommand,
                                         InventoryChangeCommand.Item outboundItem) {
        inventoryOccupyMapper.release(
                outboundCommand.getClinicCode(),
                outboundItem.skuCode(),
                outboundCommand.getOccupySerialCode(),
                outboundItem.quantity());
    }

    private void saveInventoryChangeLog(InventoryChangeCommand changeCommand, InventoryChangeCommand.Item item) {
        String clinicCode = changeCommand.getClinicCode();
        String skuCode = item.skuCode();

        Inventory balanceInventory = inventoryMapper.findOne(clinicCode, skuCode);

        InventoryLog inventoryLog = new InventoryLog();
        inventoryLog.setBusinessCode(changeCommand.getBusinessCode());
        inventoryLog.setSerialCode(changeCommand.getSerialCode());
        inventoryLog.setSkuCode(skuCode);
        inventoryLog.setQuantity(item.quantity());
        inventoryLog.setBalanceQuantity(balanceInventory.getQuantity());
        inventoryLog.setBalanceOccupyQuantity(balanceInventory.getOccupiedQuantity());
        inventoryLog.setClinicCode(clinicCode);
        inventoryLog.setChangeType(changeCommand.getChangeType());
        inventoryLog.setOperateTime(LocalDateTime.now());
        inventoryLog.setOperator(changeCommand.getOperator());
        inventoryLog.setOperatorCode(changeCommand.getOperatorCode());
        inventoryLogMapper.insert(inventoryLog);
    }

}
