package com.tyrael.kharazim.purchase.app.constant;

import com.tyrael.kharazim.basicdata.sdk.model.DictConstant;
import com.tyrael.kharazim.purchase.app.enums.PurchasePaymentStatus;
import com.tyrael.kharazim.purchase.app.enums.PurchaseReceiveStatus;

import static com.tyrael.kharazim.basicdata.sdk.model.DictConstant.dict;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
public interface PurchaseDictConstants {

    // @formatter:off

    DictConstant PURCHASE_RECEIVE_STATUS  = dict("purchase_receive_status",  PurchaseReceiveStatus.class,  "采购收货状态");
    DictConstant PURCHASE_PAYMENT_STATUS  = dict("purchase_payment_status",  PurchasePaymentStatus.class,  "采购结算状态");

    // @formatter:on

}
