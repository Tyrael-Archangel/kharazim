package com.tyrael.kharazim.application.pharmacy.service.impl;

import com.tyrael.kharazim.application.pharmacy.domain.Inventory;
import com.tyrael.kharazim.application.pharmacy.domain.InventoryLog;
import com.tyrael.kharazim.application.pharmacy.mapper.InventoryLogMapper;
import com.tyrael.kharazim.application.pharmacy.mapper.InventoryMapper;
import com.tyrael.kharazim.application.pharmacy.service.InventoryInboundService;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.InventoryChangeCommand;
import com.tyrael.kharazim.common.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/8/9
 */
@Service
@RequiredArgsConstructor
public class InventoryInboundServiceImpl implements InventoryInboundService {

    private final InventoryMapper inventoryMapper;
    private final InventoryLogMapper inventoryLogMapper;

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

}
