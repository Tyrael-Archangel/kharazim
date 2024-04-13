package com.tyrael.kharazim.application.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/4/13
 */
@Data
@TableName("file")
public class FileDO {

    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 实际文件路径
     */
    private String path;

    /**
     * ContentType
     */
    private String contentType;

    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建人编码
     */
    private String creatorCode;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
