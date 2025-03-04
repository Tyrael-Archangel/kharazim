package com.tyrael.kharazim.basicdata.app.dto.requestlog;

import com.tyrael.kharazim.basicdata.app.domain.requestlog.SystemRequestLog;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2024/12/23
 */
@Component
public class SystemRequestLogConverter {

    /**
     * SystemRequestLog -> SystemRequestLogDTO
     */
    public SystemRequestLogDTO systemRequestLogDTO(SystemRequestLog systemRequestLog) {
        return SystemRequestLogDTO.builder()
                .id(systemRequestLog.getId())
                .uri(systemRequestLog.getUri())
                .remoteAddr(systemRequestLog.getRemoteAddr())
                .forwardedFor(systemRequestLog.getForwardedFor())
                .realIp(systemRequestLog.getRealIp())
                .requestHeaders(systemRequestLog.getRequestHeaders())
                .responseHeaders(systemRequestLog.getResponseHeaders())
                .requestParams(systemRequestLog.getRequestParams())
                .responseStatus(systemRequestLog.getResponseStatus())
                .requestBody(systemRequestLog.getRequestBody())
                .responseBody(systemRequestLog.getResponseBody())
                .userName(systemRequestLog.getUserName())
                .endpoint(systemRequestLog.getEndpoint())
                .startTime(systemRequestLog.getStartTime())
                .endTime(systemRequestLog.getEndTime())
                .costMills(systemRequestLog.getCostMills())
                .build();
    }

}
