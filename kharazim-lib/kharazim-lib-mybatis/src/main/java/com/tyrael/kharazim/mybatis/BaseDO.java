package com.tyrael.kharazim.mybatis;

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

    /**
     * 逻辑删除字段，0表示未删除，删除时设置为当前时间
     */
    @TableLogic(value = "0", delval = "FLOOR(NOW(3) * 1000)")
    private Long deleted;

    public void setCreateUser(String creatorCode, String creator) {
        this.setCreateUser(creatorCode, creator, LocalDateTime.now());
    }

    public void setCreateUser(String creatorCode, String creator, LocalDateTime createTime) {
        this.creatorCode = creatorCode;
        this.creator = creator;
        this.createTime = createTime;
    }

    public void setUpdateUser(String updaterCode, String updater) {
        this.setUpdateUser(updaterCode, updater, LocalDateTime.now());
    }

    public void setUpdateUser(String updaterCode, String updater, LocalDateTime updateTime) {
        this.updaterCode = updaterCode;
        this.updater = updater;
        this.updateTime = updateTime;
    }

}
