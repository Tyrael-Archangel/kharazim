package com.tyrael.kharazim.application.settlement.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.common.dto.BaseNameAndValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Getter
@AllArgsConstructor
public enum SettlementOrderStatus implements BaseNameAndValueEnum {

    UNPAID(1, "未结算"),

    PAID(2, "已结算");

    @EnumValue
    private final Integer value;
    private final String name;

}
