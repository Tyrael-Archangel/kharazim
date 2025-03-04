package com.tyrael.kharazim.basicdata.app.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.base.dto.BaseHasNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2024/2/5
 */
@Getter
@AllArgsConstructor
public enum TransactionSourceEnum implements BaseHasNameEnum<TransactionSourceEnum> {

    SETTLEMENT_ORDER(1, "结算单"),

    CUSTOMER_RECHARGE_CARD(2, "储值单");

    @EnumValue
    private final Integer value;
    private final String name;
}
