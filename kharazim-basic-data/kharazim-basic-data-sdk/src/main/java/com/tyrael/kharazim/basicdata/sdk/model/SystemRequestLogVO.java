package com.tyrael.kharazim.basicdata.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemRequestLogVO implements Serializable {

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

    private List<NameAndValue> requestHeaders;

    private List<NameAndValue> responseHeaders;

    private List<NameAndValue> requestParams;

    private Integer responseStatus;

    private String requestBody;

    private String responseBody;

    private String userCode;

    private String userName;

    private String endpoint;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Data
    public static class NameAndValue implements Serializable {
        private String name;
        private String value;
    }
}
