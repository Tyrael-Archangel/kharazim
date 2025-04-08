package com.tyrael.kharazim.pharmacy.provider;

import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.pharmacy.app.service.InventoryService;
import com.tyrael.kharazim.pharmacy.app.vo.inventory.InventoryDTO;
import com.tyrael.kharazim.pharmacy.app.vo.inventory.ListInventoryOfClinicRequest;
import com.tyrael.kharazim.pharmacy.sdk.model.InventoryVO;
import com.tyrael.kharazim.pharmacy.sdk.service.InventoryQueryServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/4/8
 */
@Component
@DubboService
@RequiredArgsConstructor
public class InventoryQueryServiceProvider implements InventoryQueryServiceApi {

    private final InventoryService inventoryService;

    @Override
    public List<InventoryVO> queryInventories(String clinicCode, Collection<String> skuCodes) {
        if (CollectionUtils.isEmpty(skuCodes) || !StringUtils.hasText(clinicCode)) {
            return new ArrayList<>();
        }

        ListInventoryOfClinicRequest listRequest = new ListInventoryOfClinicRequest(clinicCode, new HashSet<>(skuCodes));
        List<InventoryDTO> inventories = inventoryService.listOfClinic(listRequest);
        return inventories.stream()
                .map(e -> new InventoryVO()
                        .setClinicCode(e.getClinicCode())
                        .setSkuCode(e.getSkuCode())
                        .setQuantity(e.getQuantity())
                        .setOccupiedQuantity(e.getOccupiedQuantity())
                        .setUsableQuantity(e.getUsableQuantity()))
                .collect(Collectors.toList());
    }

}
