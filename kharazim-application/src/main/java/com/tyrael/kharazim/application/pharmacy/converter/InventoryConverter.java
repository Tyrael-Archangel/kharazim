package com.tyrael.kharazim.application.pharmacy.converter;

import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.pharmacy.domain.Inventory;
import com.tyrael.kharazim.application.pharmacy.domain.InventoryLog;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.InventoryLogVO;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.InventoryVO;
import com.tyrael.kharazim.application.product.service.ProductSkuRepository;
import com.tyrael.kharazim.application.product.vo.sku.ProductSkuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Component
@RequiredArgsConstructor
public class InventoryConverter {

    private final ProductSkuRepository skuRepository;
    private final ClinicMapper clinicMapper;

    /**
     * Inventories -> InventoryVOs
     */
    public List<InventoryVO> inventories(Collection<Inventory> inventories) {
        if (inventories == null || inventories.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> clinicCodes = new HashSet<>();
        Set<String> skuCodes = new HashSet<>();
        for (Inventory inventory : inventories) {
            clinicCodes.add(inventory.getClinicCode());
            skuCodes.add(inventory.getSkuCode());
        }
        Map<String, Clinic> clinicMap = clinicMapper.mapByCodes(clinicCodes);
        Map<String, ProductSkuVO> skuMap = skuRepository.mapByCodes(skuCodes);

        return inventories.stream()
                .map(inventory -> {
                    Clinic clinic = clinicMap.get(inventory.getClinicCode());
                    ProductSkuVO skuVO = skuMap.get(inventory.getSkuCode());
                    return InventoryVO.builder()
                            .clinicCode(clinic.getCode())
                            .clinicName(clinic.getName())
                            .skuCode(skuVO.getCode())
                            .skuName(skuVO.getName())
                            .categoryCode(skuVO.getCategoryCode())
                            .categoryName(skuVO.getCategoryName())
                            .unitCode(skuVO.getUnitCode())
                            .unitName(skuVO.getUnitName())
                            .defaultImageUrl(skuVO.getDefaultImageUrl())
                            .description(skuVO.getDescription())
                            .quantity(inventory.getQuantity())
                            .occupiedQuantity(inventory.getOccupiedQuantity())
                            .usableQuantity(inventory.getUsableQuantity())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * InventoryLogs -> InventoryLogVOs
     */
    public List<InventoryLogVO> inventoryLogs(Collection<InventoryLog> inventoryLogs) {
        if (inventoryLogs == null || inventoryLogs.isEmpty()) {
            return new ArrayList<>();
        }
        Set<String> clinicCodes = new HashSet<>();
        Set<String> skuCodes = new HashSet<>();
        for (InventoryLog inventory : inventoryLogs) {
            clinicCodes.add(inventory.getClinicCode());
            skuCodes.add(inventory.getSkuCode());
        }
        Map<String, Clinic> clinicMap = clinicMapper.mapByCodes(clinicCodes);
        Map<String, ProductSkuVO> skuMap = skuRepository.mapByCodes(skuCodes);

        return inventoryLogs.stream()
                .map(inventoryLog -> {
                    Clinic clinic = clinicMap.get(inventoryLog.getClinicCode());
                    ProductSkuVO skuVO = skuMap.get(inventoryLog.getSkuCode());

                    return InventoryLogVO.builder()
                            .id(inventoryLog.getId())
                            .businessCode(inventoryLog.getBusinessCode())
                            .serialCode(inventoryLog.getSerialCode())
                            .skuCode(skuVO.getCode())
                            .skuName(skuVO.getName())
                            .categoryCode(skuVO.getCategoryCode())
                            .categoryName(skuVO.getCategoryName())
                            .unitCode(skuVO.getUnitCode())
                            .unitName(skuVO.getUnitName())
                            .defaultImageUrl(skuVO.getDefaultImageUrl())
                            .description(skuVO.getDescription())
                            .quantity(inventoryLog.getQuantity())
                            .balanceQuantity(inventoryLog.getBalanceQuantity())
                            .balanceOccupyQuantity(inventoryLog.getBalanceOccupyQuantity())
                            .clinicCode(clinic.getCode())
                            .clinicName(clinic.getName())
                            .changeType(inventoryLog.getChangeType())
                            .operateTime(inventoryLog.getOperateTime())
                            .operator(inventoryLog.getOperator())
                            .operatorCode(inventoryLog.getOperatorCode())
                            .build();
                })
                .collect(Collectors.toList());
    }

}
