package com.tyrael.kharazim.application.pharmacy.service.impl;

import com.tyrael.kharazim.application.pharmacy.converter.InventoryConverter;
import com.tyrael.kharazim.application.pharmacy.domain.Inventory;
import com.tyrael.kharazim.application.pharmacy.domain.InventoryLog;
import com.tyrael.kharazim.application.pharmacy.mapper.InventoryLogMapper;
import com.tyrael.kharazim.application.pharmacy.mapper.InventoryMapper;
import com.tyrael.kharazim.application.pharmacy.service.InventoryService;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.InventoryChangeCommand;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.InventoryVO;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.ListInventoryOfClinicRequest;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.PageInventoryRequest;
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
    public PageResponse<InventoryVO> page(PageInventoryRequest pageRequest) {
        PageResponse<Inventory> pageData = inventoryMapper.page(pageRequest);
        return PageResponse.success(
                inventoryConverter.inventories(pageData.getData()),
                pageData.getTotalCount(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }

    @Override
    public List<InventoryVO> listOfClinic(ListInventoryOfClinicRequest listRequest) {
        List<Inventory> inventories = inventoryMapper.listOfClinic(listRequest);
        return inventoryConverter.inventories(inventories);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void occupyByPrescription(Prescription prescription) {

        prescription.getProducts()
                .stream()
                .sorted(Comparator.comparing(PrescriptionProduct::getSkuCode))
                .forEach(e -> {
                    int updatedRow = inventoryMapper.increaseOccupy(
                            prescription.getClinicCode(),
                            e.getSkuCode(),
                            e.getQuantity());
                    BusinessException.assertTrue(updatedRow > 0, "商品库存不足");
                });

        // save occupy log
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inbound(InventoryChangeCommand inboundCommand) {
        String businessCode = inboundCommand.businessCode();
        String clinicCode = inboundCommand.clinicCode();

        List<InventoryChangeCommand.Item> sortedItems = CollectionUtils.safeStream(inboundCommand.items())
                .sorted(Comparator.comparing(InventoryChangeCommand.Item::skuCode))
                .toList();

        for (InventoryChangeCommand.Item inboundItem : sortedItems) {
            String skuCode = inboundItem.skuCode();
            Integer quantity = inboundItem.quantity();

            // 增加库存
            int row = inventoryMapper.increaseQuantity(skuCode, clinicCode, quantity);
            if (row <= 0) {
                // 库存数据不存在
                Inventory inventory = new Inventory();
                inventory.setClinicCode(clinicCode);
                inventory.setSkuCode(skuCode);
                inventory.setQuantity(quantity);
                inventory.setOccupiedQuantity(0);
                inventoryMapper.insert(inventory);
            }

            // 保存日志
            // noinspection DuplicatedCode
            InventoryLog inventoryLog = new InventoryLog();
            inventoryLog.setSourceBusinessCode(businessCode);
            inventoryLog.setSkuCode(skuCode);
            inventoryLog.setQuantity(quantity);
            inventoryLog.setClinicCode(clinicCode);
            inventoryLog.setOutInType(inboundCommand.outInType());
            inventoryLog.setOperateTime(LocalDateTime.now());
            inventoryLog.setOperator(inboundCommand.operatorCode());
            inventoryLog.setOperatorCode(inboundCommand.operatorCode());
            inventoryLogMapper.insert(inventoryLog);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void outbound(InventoryChangeCommand outboundCommand) {
        String businessCode = outboundCommand.businessCode();
        String clinicCode = outboundCommand.clinicCode();

        List<InventoryChangeCommand.Item> sortedItems = CollectionUtils.safeStream(outboundCommand.items())
                .sorted(Comparator.comparing(InventoryChangeCommand.Item::skuCode))
                .toList();

        for (InventoryChangeCommand.Item outboundItem : sortedItems) {
            String skuCode = outboundItem.skuCode();
            Integer quantity = outboundItem.quantity();

            // 扣减库存和预占
            inventoryMapper.decreaseQuantityByOccupy(skuCode, clinicCode, quantity);

            // 保存日志
            // noinspection DuplicatedCode
            InventoryLog inventoryLog = new InventoryLog();
            inventoryLog.setSourceBusinessCode(businessCode);
            inventoryLog.setSkuCode(skuCode);
            inventoryLog.setQuantity(quantity);
            inventoryLog.setClinicCode(clinicCode);
            inventoryLog.setOutInType(outboundCommand.outInType());
            inventoryLog.setOperateTime(LocalDateTime.now());
            inventoryLog.setOperator(outboundCommand.operatorCode());
            inventoryLog.setOperatorCode(outboundCommand.operatorCode());
            inventoryLogMapper.insert(inventoryLog);
        }
    }

}
