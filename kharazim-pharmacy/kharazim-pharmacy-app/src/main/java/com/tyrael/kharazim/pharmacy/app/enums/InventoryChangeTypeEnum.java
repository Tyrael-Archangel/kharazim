package com.tyrael.kharazim.pharmacy.app.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.base.dto.BaseHasNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2024/8/9
 */
@Getter
@AllArgsConstructor
public enum InventoryChangeTypeEnum implements BaseHasNameEnum<InventoryChangeTypeEnum> {

    PURCHASE_IN(1, "采购入库"),

    SALE_OUT(2, "销售出库"),

    SALE_OCCUPY(3, "销售预占");

    @EnumValue
    private final Integer value;
    private final String name;

}
