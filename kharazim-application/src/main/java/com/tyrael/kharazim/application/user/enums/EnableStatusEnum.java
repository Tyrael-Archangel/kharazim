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
public enum EnableStatusEnum implements BaseNameAndValueEnum {

    /**
     * 已启用
     */
    ENABLED(1, "已启用"),

    /**
     * 已禁用
     */
    DISABLED(0, "已禁用");

    @EnumValue
    private final Integer value;
    private final String name;

    public static EnableStatusEnum ofValueDefaultEnable(Integer value) {
        for (EnableStatusEnum enableStatusEnum : values()) {
            if (enableStatusEnum.getValue().equals(value)) {
                return enableStatusEnum;
            }
        }
        return EnableStatusEnum.ENABLED;
    }

}
