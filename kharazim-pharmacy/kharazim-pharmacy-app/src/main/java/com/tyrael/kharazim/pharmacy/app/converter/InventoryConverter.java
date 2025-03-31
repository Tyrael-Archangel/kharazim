package com.tyrael.kharazim.pharmacy.app.converter;

import com.tyrael.kharazim.basicdata.sdk.model.ClinicVO;
import com.tyrael.kharazim.basicdata.sdk.service.ClinicServiceApi;
import com.tyrael.kharazim.pharmacy.app.domain.Inventory;
import com.tyrael.kharazim.pharmacy.app.domain.InventoryLog;
import com.tyrael.kharazim.pharmacy.app.vo.inventory.InventoryLogVO;
import com.tyrael.kharazim.pharmacy.app.vo.inventory.InventoryVO;
import com.tyrael.kharazim.product.sdk.model.ProductSkuVO;
import com.tyrael.kharazim.product.sdk.service.ProductServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
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

    @DubboReference
    private ClinicServiceApi clinicServiceApi;

    @DubboReference
    private ProductServiceApi productServiceApi;

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
        Map<String, ClinicVO> clinicMap = clinicServiceApi.mapByCodes(clinicCodes);
        Map<String, ProductSkuVO> skuMap = productServiceApi.mapByCodes(skuCodes);

        return inventories.stream()
                .map(inventory -> {
                    ClinicVO clinic = clinicMap.get(inventory.getClinicCode());
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
                            .defaultImage(skuVO.getDefaultImage())
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
        Map<String, ClinicVO> clinicMap = clinicServiceApi.mapByCodes(clinicCodes);
        Map<String, ProductSkuVO> skuMap = productServiceApi.mapByCodes(skuCodes);

        return inventoryLogs.stream()
                .map(inventoryLog -> {
                    ClinicVO clinic = clinicMap.get(inventoryLog.getClinicCode());
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
                            .defaultImage(skuVO.getDefaultImage())
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
