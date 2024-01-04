package com.tyrael.kharazim.application.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
public class Role {

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

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 逻辑删除，0表示未删除，时间戳表示删除时间
     */
    private Long deletedTimestamp;

    public boolean isDeleted() {
        return deletedTimestamp != 0L;
    }

    public boolean isAdmin() {
        return Boolean.TRUE.equals(superAdmin);
    }
}
