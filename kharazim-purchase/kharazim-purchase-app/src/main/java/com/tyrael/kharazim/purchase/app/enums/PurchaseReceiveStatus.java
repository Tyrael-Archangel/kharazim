package com.tyrael.kharazim.purchase.app.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.base.dto.BaseHasNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2024/5/31
 */
@Getter
@AllArgsConstructor
public enum PurchaseReceiveStatus implements BaseHasNameEnum<PurchaseReceiveStatus> {

    WAIT_RECEIVE(1, "待收货"),

    RECEIVING(2, "收货中"),

    RECEIVE_FINISHED(3, "收货完结");

    @EnumValue
    private final Integer value;
    private final String name;

}
