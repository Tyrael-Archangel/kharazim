package com.tyrael.kharazim.application.purchase.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.common.dto.BaseHasNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2024/5/31
 */
@Getter
@AllArgsConstructor
public enum PurchasePaymentStatus implements BaseHasNameEnum<PurchasePaymentStatus> {

    UNPAID(1, "待结算"),
    PART_PAID(2, "部分结算"),
    ALL_PAID(3, "已结算");

    @EnumValue
    private final Integer value;
    private final String name;

}
