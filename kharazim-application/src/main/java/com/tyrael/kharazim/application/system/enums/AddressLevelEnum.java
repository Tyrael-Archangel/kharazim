package com.tyrael.kharazim.application.system.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.common.dto.BaseNameAndValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@Getter
@AllArgsConstructor
public enum AddressLevelEnum implements BaseNameAndValueEnum {

    /**
     * 省
     */
    PROVINCE(1, "省"),

    /**
     * 市
     */
    CITY(2, "市"),

    /**
     * 县（区）
     */
    COUNTY(3, "县（区）");

    @EnumValue
    private final Integer value;
    private final String name;
}
