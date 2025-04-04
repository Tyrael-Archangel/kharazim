package com.tyrael.kharazim.pharmacy.app.constant;

import com.tyrael.kharazim.basicdata.sdk.model.DictConstant;
import com.tyrael.kharazim.pharmacy.app.enums.InventoryChangeTypeEnum;

import static com.tyrael.kharazim.basicdata.sdk.model.DictConstant.dict;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
public interface PharmacyDictConstants {

    // @formatter:off

    DictConstant INVENTORY_CHANGE_TYPE  = dict("inventory_change_type",  InventoryChangeTypeEnum.class,  "库存变化类型");

    // @formatter:on

}
