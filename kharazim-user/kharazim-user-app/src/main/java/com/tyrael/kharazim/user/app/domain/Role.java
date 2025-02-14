package com.tyrael.kharazim.user.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.mybatis.BaseDO;
import com.tyrael.kharazim.user.app.enums.EnableStatusEnum;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
public class Role extends BaseDO {

    public static final String SUPER_ADMIN_CODE = "SUPER_ADMIN";

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 岗位编号
     */
    private String code;

    /**
     * 是否为超管
     */
    private Boolean superAdmin;

    /**
     * 角色（岗位）名
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 启用状态
     */
    private EnableStatusEnum status;

    public boolean isAdmin() {
        return Boolean.TRUE.equals(superAdmin);
    }
}
