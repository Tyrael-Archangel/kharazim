package com.tyrael.kharazim.application.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.tyrael.kharazim.application.system.dto.requestlog.NameAndValue;
import com.tyrael.kharazim.application.system.typehandler.ListNameValueTypeHandler;
import lombok.Data;
import org.checkerframework.checker.units.qual.N;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    @TableField(typeHandler = ListNameValueTypeHandler.class)
    private List<NameAndValue> requestHeaders;

    @TableField(typeHandler = ListNameValueTypeHandler.class)
    private List<NameAndValue> responseHeaders;

    @TableField(typeHandler = ListNameValueTypeHandler.class)
    private List<NameAndValue> requestParams;

    private Integer responseStatus;

    private String requestBody;

    private String responseBody;

    private String userName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @TableField(insertStrategy = FieldStrategy.NEVER)
    public Long costMills;

//    public Long getCostMills() {
//        if (endTime == null || startTime == null) {
//            return null;
//        }
//        return startTime.until(endTime, ChronoUnit.MILLIS);
//    }

}
