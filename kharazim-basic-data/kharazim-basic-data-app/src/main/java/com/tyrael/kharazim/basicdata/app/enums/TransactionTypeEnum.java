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
public enum TransactionTypeEnum implements BaseHasNameEnum<TransactionTypeEnum> {

    RECHARGE(1, "充值"),

    CONSUME(2, "消费"),

    REFUND(3, "退款");

    @EnumValue
    private final Integer value;
    private final String name;
}
