package com.tyrael.kharazim.application.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@TableName("`dict`")
public class Dict {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String code;

    private String name;

    private Boolean enabled;

    private String remark;

    /**
     * 是否为系统内置字典
     */
    private Boolean systemDict;

    /**
     * 是否允许修改字典项
     */
    private Boolean allowModifyItem;

    private String creator;
    private String creatorCode;
    private LocalDateTime createTime;
    private String updater;
    private String updaterCode;
    private LocalDateTime updateTime;

    public boolean systemInnerDict() {
        return Boolean.TRUE.equals(systemDict);
    }

    public boolean forbiddenModifyItem() {
        return !Boolean.TRUE.equals(allowModifyItem);
    }

}
