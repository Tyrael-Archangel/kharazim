package com.tyrael.kharazim.application.skupublish.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.common.dto.BaseHasNameEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2024/4/28
 */
@Getter
@RequiredArgsConstructor
public enum SkuPublishStatus implements BaseHasNameEnum<SkuPublishStatus> {

    WAIT_EFFECT(1, "待生效"),

    IN_EFFECT(2, "生效中"),

    LOST_EFFECT(3, "已失效"),

    CANCELED(4, "已取消");

    @EnumValue
    private final Integer value;
    private final String name;

}
