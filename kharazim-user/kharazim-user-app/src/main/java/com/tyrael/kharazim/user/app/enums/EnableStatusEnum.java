package com.tyrael.kharazim.user.app.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.lib.base.dto.BaseHasNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Getter
@AllArgsConstructor
public enum EnableStatusEnum implements BaseHasNameEnum<EnableStatusEnum> {

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

}
