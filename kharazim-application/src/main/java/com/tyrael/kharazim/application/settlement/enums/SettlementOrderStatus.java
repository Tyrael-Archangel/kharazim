package com.tyrael.kharazim.application.settlement.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.common.dto.BaseHasNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Getter
@AllArgsConstructor
public enum SettlementOrderStatus implements BaseHasNameEnum<SettlementOrderStatus> {

    UNPAID(1, "待结算"),

    PAID(2, "已结算");

    @EnumValue
    private final Integer value;
    private final String name;

}
