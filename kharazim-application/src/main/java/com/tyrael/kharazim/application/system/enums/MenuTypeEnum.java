package com.tyrael.kharazim.application.system.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.common.dto.BaseHasNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
@Getter
@AllArgsConstructor
public enum MenuTypeEnum implements BaseHasNameEnum<MenuTypeEnum> {

    /**
     * 菜单
     */
    MENU(1, "菜单"),
    /**
     * 目录
     */
    CATALOG(2, "目录"),
    /**
     * 外链
     */
    EXTLINK(3, "外链"),
    /**
     * 按钮
     */
    BUTTON(4, "按钮");

    public static final String DESC = "菜单类型，MENU-菜单，CATALOG-目录，EXTLINK-外链，BUTTON-按钮";

    @EnumValue
    private final Integer value;
    private final String name;
}
