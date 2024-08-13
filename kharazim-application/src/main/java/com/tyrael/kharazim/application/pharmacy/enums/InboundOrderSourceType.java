package com.tyrael.kharazim.application.pharmacy.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.common.dto.BaseNameAndValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2024/8/12
 */
@Getter
@AllArgsConstructor
public enum InboundOrderSourceType implements BaseNameAndValueEnum {

    PURCHASE_ORDER(1, "采购单");

    @EnumValue
    private final Integer value;
    private final String name;
}
