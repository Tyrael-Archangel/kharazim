package com.tyrael.kharazim.finance.app.constant;

import com.tyrael.kharazim.basicdata.sdk.model.DictConstant;
import com.tyrael.kharazim.finance.app.enums.CustomerRechargeCardStatus;

import static com.tyrael.kharazim.basicdata.sdk.model.DictConstant.dict;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
public interface FinanceDictConstants {

    // @formatter:off

    DictConstant CUSTOMER_RECHARGE_STATUS  = dict("customer_recharge_status",   CustomerRechargeCardStatus.class,   "会员储值单状态");

    // @formatter:on

}
