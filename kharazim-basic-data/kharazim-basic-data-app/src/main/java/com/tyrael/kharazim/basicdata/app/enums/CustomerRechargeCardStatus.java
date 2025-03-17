package com.tyrael.kharazim.basicdata.app.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.base.dto.BaseHasNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2024/2/1
 */
@Getter
@AllArgsConstructor
public enum CustomerRechargeCardStatus implements BaseHasNameEnum<CustomerRechargeCardStatus> {

    UNPAID(1, "未收款"),

    PAID(2, "已收款"),

    WAIT_REFUND(3, "未退款"),

    REFUNDED(4, "已退款");

    public static final String DESC = "UNPAID-未收款，PAID-已收款，WAIT_REFUND-未退款，REFUNDED-已退款";

    @EnumValue
    private final Integer value;
    private final String name;

}
