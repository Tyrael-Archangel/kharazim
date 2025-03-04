package com.tyrael.kharazim.basicdata.app.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.base.dto.BaseHasNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2025/3/3
 */
@Getter
@AllArgsConstructor
public enum CustomerGenderEnum implements BaseHasNameEnum<CustomerGenderEnum> {

    /**
     * 男性
     */
    MALE(1, "男", "male"),

    /**
     * 女性
     */
    FEMALE(2, "女", "female");

    @EnumValue
    private final Integer value;
    private final String name;
    private final String englishName;

}
