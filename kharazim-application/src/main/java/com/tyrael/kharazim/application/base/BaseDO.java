package com.tyrael.kharazim.application.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
public class BaseDO {

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String creator;

    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String creatorCode;

    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updater;

    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updaterCode;

    @TableLogic
    private Boolean deleted;

    public void setUpdate(String updaterCode, String updater) {
        this.setUpdate(updaterCode, updater, LocalDateTime.now());
    }

    public void setUpdate(String updaterCode, String updater, LocalDateTime updateTime) {
        this.updaterCode = updaterCode;
        this.updater = updater;
        this.updateTime = updateTime;
    }

}
