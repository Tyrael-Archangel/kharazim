package com.tyrael.kharazim.pharmacy.app.constant;

import com.tyrael.kharazim.basicdata.sdk.model.DictConstant;
import com.tyrael.kharazim.pharmacy.app.enums.InboundOrderStatus;
import com.tyrael.kharazim.pharmacy.app.enums.InventoryChangeTypeEnum;
import com.tyrael.kharazim.pharmacy.app.enums.OutboundOrderStatus;

import static com.tyrael.kharazim.basicdata.sdk.model.DictConstant.dict;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
public interface PharmacyDictConstants {

    // @formatter:off

    DictConstant INVENTORY_CHANGE_TYPE  = dict("inventory_change_type",  InventoryChangeTypeEnum.class,  "库存变化类型");
    DictConstant INBOUND_ORDER_STATUS   = dict("inbound_order_status",   InboundOrderStatus.class,       "入库单状态");
    DictConstant OUTBOUND_ORDER_STATUS  = dict("outbound_order_status",  OutboundOrderStatus.class,      "出库单状态");

    // @formatter:on

}
