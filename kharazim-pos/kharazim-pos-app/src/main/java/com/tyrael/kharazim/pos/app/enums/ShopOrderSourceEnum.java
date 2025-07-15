package com.tyrael.kharazim.pos.app.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.base.dto.BaseHasNameEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2025/7/15
 */
@Getter
@RequiredArgsConstructor
public enum ShopOrderSourceEnum implements BaseHasNameEnum<ShopOrderSourceEnum> {

    POS("POS");

    @EnumValue
    private final String name;

}
