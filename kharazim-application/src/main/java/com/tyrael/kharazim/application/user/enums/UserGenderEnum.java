package com.tyrael.kharazim.application.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.common.dto.BaseNameAndValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Getter
@AllArgsConstructor
public enum UserGenderEnum implements BaseNameAndValueEnum {

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
