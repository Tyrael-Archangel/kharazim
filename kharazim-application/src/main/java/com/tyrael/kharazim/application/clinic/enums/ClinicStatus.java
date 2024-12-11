package com.tyrael.kharazim.application.clinic.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.common.dto.BaseHasNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2023/12/30
 */
@Getter
@AllArgsConstructor
public enum ClinicStatus implements BaseHasNameEnum<ClinicStatus> {

    NORMAL(1, "正常经营"),

    CLOSED(2, "已关闭");

    @EnumValue
    private final Integer value;
    private final String name;
}
