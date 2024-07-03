package com.tyrael.kharazim.application.pharmacy.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.common.dto.BaseNameAndValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2024/7/3
 */
@Getter
@AllArgsConstructor
public enum InboundOrderStatus implements BaseNameAndValueEnum {

    WAIT_RECEIVE(1, "待收货"),

    RECEIVING(2, "收货中"),

    RECEIVE_FINISHED(3, "收货完结");

    @EnumValue
    private final Integer value;
    private final String name;

}
