package com.tyrael.kharazim.basicdata.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.tyrael.kharazim.basicdata.app.dto.requestlog.NameAndValue;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
@Data
@TableName(autoResultMap = true)
public class SystemRequestLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String uri;

    private String remoteAddr;

    /**
     * nginx代理添加的X-Forwarded-For
     */
    private String forwardedFor;

    /**
     * nginx代理添加的X-Real-IP
     */
    private String realIp;

    private String httpMethod;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<NameAndValue> requestHeaders;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<NameAndValue> responseHeaders;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<NameAndValue> requestParams;

    private Integer responseStatus;

    private String requestBody;

    private String responseBody;

    private String userCode;

    private String userName;

    private String endpoint;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Long costMills;

}
