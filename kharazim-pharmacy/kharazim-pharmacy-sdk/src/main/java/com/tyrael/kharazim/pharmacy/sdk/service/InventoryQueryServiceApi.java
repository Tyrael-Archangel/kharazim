package com.tyrael.kharazim.pharmacy.sdk.service;

import com.tyrael.kharazim.pharmacy.sdk.model.InventoryVO;

import java.util.Collection;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/4/8
 */
public interface InventoryQueryServiceApi {

    /**
     * query inventories
     *
     * @param clinicCode 诊所编码
     * @param skuCodes   skuCodes
     * @return inventories
     */
    List<InventoryVO> queryInventories(String clinicCode, Collection<String> skuCodes);

}
