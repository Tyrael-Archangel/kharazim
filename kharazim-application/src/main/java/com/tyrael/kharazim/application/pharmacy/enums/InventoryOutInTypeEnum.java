package com.tyrael.kharazim.application.pharmacy.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.common.dto.BaseNameAndValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2024/8/9
 */
@Getter
@AllArgsConstructor
public enum InventoryOutInTypeEnum implements BaseNameAndValueEnum {

    PURCHASE_IN(1, "采购入库"),

    SALE_OUT(2, "销售出库");

    @EnumValue
    private final Integer value;
    private final String name;

}
