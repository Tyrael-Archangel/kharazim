package com.tyrael.kharazim.pharmacy.app.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.base.dto.BaseHasNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2024/8/12
 */
@Getter
@AllArgsConstructor
public enum InboundOrderSourceType implements BaseHasNameEnum<InboundOrderSourceType> {

    PURCHASE_ORDER(1, "采购单");

    @EnumValue
    private final Integer value;
    private final String name;
}
