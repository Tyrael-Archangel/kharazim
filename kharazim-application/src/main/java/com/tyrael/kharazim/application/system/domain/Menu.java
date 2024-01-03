package com.tyrael.kharazim.application.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tyrael.kharazim.application.system.enums.MenuTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("`menu`")
public class Menu {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long parentId;

    private String name;

    private String icon;

    private String path;

    private String component;

    private Integer sort;

    private Boolean visible;

    private String redirect;

    private MenuTypeEnum menuType;

    /**
     * 权限标识
     */
    private String perm;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
