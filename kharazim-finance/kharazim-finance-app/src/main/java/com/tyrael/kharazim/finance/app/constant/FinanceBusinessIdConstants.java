package com.tyrael.kharazim.finance.app.constant;

import com.tyrael.kharazim.lib.idgenerator.BusinessIdConstant;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2025/2/18
 */
@Getter
@SuppressWarnings("unused")
public enum FinanceBusinessIdConstants implements BusinessIdConstant<FinanceBusinessIdConstants> {

    CUSTOMER_RECHARGE_CARD("会员储值单", "CRC"),
    CUSTOMER_WALLET_TRANSACTION("会员交易流水号编码", "TRS"),
    RECHARGE_CARD_TYPE("储值卡项", "RCT"),
    SETTLEMENT_ORDER("结算单", "BO"),
    ;

    private final String prefix;
    private final String desc;
    private final int bit;

    FinanceBusinessIdConstants(String desc) {
        this(desc, DEFAULT_BIT);
    }

    FinanceBusinessIdConstants(String desc, int bit) {
        this(desc, bit, null);
    }

    FinanceBusinessIdConstants(String desc, String prefix) {
        this(desc, DEFAULT_BIT, prefix);
    }

    FinanceBusinessIdConstants(String desc, int bit, String prefix) {
        this.prefix = prefix;
        this.desc = desc;
        this.bit = bit;
    }

}
