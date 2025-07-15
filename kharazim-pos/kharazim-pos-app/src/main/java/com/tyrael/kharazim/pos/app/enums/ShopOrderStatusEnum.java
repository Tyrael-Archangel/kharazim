package com.tyrael.kharazim.pos.app.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.base.dto.BaseHasNameEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2025/7/15
 */
@Getter
@RequiredArgsConstructor
public enum ShopOrderStatusEnum implements BaseHasNameEnum<ShopOrderStatusEnum> {

    UNPAID(1, "待付款"),
    CANCEL(2, "已取消"),
    COMPLETED(3, "已完成"),
    AFTER_SALE(4, "已售后");

    @EnumValue
    private final Integer value;
    private final String name;

}
