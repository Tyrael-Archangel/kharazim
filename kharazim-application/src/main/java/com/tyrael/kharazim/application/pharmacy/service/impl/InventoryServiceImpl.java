package com.tyrael.kharazim.application.pharmacy.service.impl;

import com.tyrael.kharazim.application.pharmacy.converter.InventoryConverter;
import com.tyrael.kharazim.application.pharmacy.domain.Inventory;
import com.tyrael.kharazim.application.pharmacy.domain.InventoryLog;
import com.tyrael.kharazim.application.pharmacy.enums.InventoryChangeTypeEnum;
import com.tyrael.kharazim.application.pharmacy.mapper.InventoryLogMapper;
import com.tyrael.kharazim.application.pharmacy.mapper.InventoryMapper;
import com.tyrael.kharazim.application.pharmacy.service.InventoryService;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.*;
import com.tyrael.kharazim.application.prescription.domain.Prescription;
import com.tyrael.kharazim.application.prescription.domain.PrescriptionProduct;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryMapper inventoryMapper;
    private final InventoryLogMapper inventoryLogMapper;
    private final InventoryConverter inventoryConverter;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InventoryVO> page(PageInventoryRequest pageRequest) {
        PageResponse<Inventory> pageData = inventoryMapper.page(pageRequest);
        return PageResponse.success(
                inventoryConverter.inventories(pageData.getData()),
                pageData.getTotalCount(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryVO> listOfClinic(ListInventoryOfClinicRequest listRequest) {
        List<Inventory> inventories = inventoryMapper.listOfClinic(listRequest);
        return inventoryConverter.inventories(inventories);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InventoryLogVO> pageLog(PageInventoryLogRequest pageRequest) {
        PageResponse<InventoryLog> pageData = inventoryLogMapper.page(pageRequest);
        return PageResponse.success(
                inventoryConverter.inventoryLogs(pageData.getData()),
                pageData.getTotalCount(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void occupyByPrescription(Prescription prescription) {

        List<PrescriptionProduct> sortedProducts = prescription.getProducts()
                .stream()
                .sorted(Comparator.comparing(PrescriptionProduct::getSkuCode))
                .toList();

        for (PrescriptionProduct product : sortedProducts) {
            int updatedRow = inventoryMapper.increaseOccupy(
                    prescription.getClinicCode(),
                    product.getSkuCode(),
                    product.getQuantity());
            BusinessException.assertTrue(updatedRow > 0, "商品库存不足");

            // save occupy log
            saveOccupyInventoryLog(prescription, product);
        }
    }

    private void saveOccupyInventoryLog(Prescription prescription, PrescriptionProduct product) {
        String clinicCode = prescription.getClinicCode();
        String skuCode = product.getSkuCode();

        Inventory balanceInventory = inventoryMapper.findOne(clinicCode, skuCode);

        InventoryLog inventoryLog = new InventoryLog();
        inventoryLog.setSourceBusinessCode(prescription.getCode());
        inventoryLog.setSkuCode(skuCode);
        inventoryLog.setQuantity(product.getQuantity());
        inventoryLog.setBalanceQuantity(balanceInventory.getQuantity());
        inventoryLog.setBalanceOccupyQuantity(balanceInventory.getOccupiedQuantity());
        inventoryLog.setClinicCode(clinicCode);
        inventoryLog.setChangeType(InventoryChangeTypeEnum.SALE_OCCUPY);
        inventoryLog.setOperateTime(LocalDateTime.now());
        inventoryLog.setOperator(prescription.getCreator());
        inventoryLog.setOperatorCode(prescription.getCreatorCode());
        inventoryLogMapper.insert(inventoryLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inbound(InventoryChangeCommand inboundCommand) {

        List<InventoryChangeCommand.Item> sortedItems = CollectionUtils.safeStream(inboundCommand.items())
                .sorted(Comparator.comparing(InventoryChangeCommand.Item::skuCode))
                .toList();

        for (InventoryChangeCommand.Item inboundItem : sortedItems) {
            String skuCode = inboundItem.skuCode();
            Integer quantity = inboundItem.quantity();

            // 增加库存
            int row = inventoryMapper.increaseQuantity(inboundCommand.clinicCode(), skuCode, quantity);
            if (row <= 0) {
                // 库存数据不存在
                Inventory inventory = new Inventory();
                inventory.setClinicCode(inboundCommand.clinicCode());
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
    public void outbound(InventoryChangeCommand outboundCommand) {

        List<InventoryChangeCommand.Item> sortedItems = CollectionUtils.safeStream(outboundCommand.items())
                .sorted(Comparator.comparing(InventoryChangeCommand.Item::skuCode))
                .toList();

        for (InventoryChangeCommand.Item outboundItem : sortedItems) {
            // 扣减库存和预占
            inventoryMapper.decreaseQuantityByOccupy(
                    outboundCommand.clinicCode(),
                    outboundItem.skuCode(),
                    outboundItem.quantity());

            // 保存库存日志
            saveInventoryChangeLog(outboundCommand, outboundItem);
        }
    }

    private void saveInventoryChangeLog(InventoryChangeCommand changeCommand, InventoryChangeCommand.Item changeItem) {

        String clinicCode = changeCommand.clinicCode();
        String skuCode = changeItem.skuCode();

        Inventory balanceInventory = inventoryMapper.findOne(clinicCode, skuCode);

        InventoryLog inventoryLog = new InventoryLog();
        inventoryLog.setSourceBusinessCode(changeCommand.businessCode());
        inventoryLog.setSkuCode(skuCode);
        inventoryLog.setQuantity(changeItem.quantity());
        inventoryLog.setBalanceQuantity(balanceInventory.getQuantity());
        inventoryLog.setBalanceOccupyQuantity(balanceInventory.getOccupiedQuantity());
        inventoryLog.setClinicCode(clinicCode);
        inventoryLog.setChangeType(changeCommand.changeType());
        inventoryLog.setOperateTime(LocalDateTime.now());
        inventoryLog.setOperator(changeCommand.operator());
        inventoryLog.setOperatorCode(changeCommand.operatorCode());
        inventoryLogMapper.insert(inventoryLog);
    }

}
