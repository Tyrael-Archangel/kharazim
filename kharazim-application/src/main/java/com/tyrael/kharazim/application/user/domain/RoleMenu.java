package com.tyrael.kharazim.application.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/4
 */
@Data
public class RoleMenu {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色（岗位）ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

}
