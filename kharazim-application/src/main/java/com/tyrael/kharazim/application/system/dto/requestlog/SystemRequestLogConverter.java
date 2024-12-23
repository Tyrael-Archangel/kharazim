package com.tyrael.kharazim.application.system.dto.requestlog;

import com.tyrael.kharazim.application.system.domain.SystemRequestLog;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.pattern.PathPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * string value of {@linkplain RequestMappingInfo requestMappingInfo}
     */
    public String requestMappingInfoEndpoint(RequestMappingInfo requestMappingInfo) {
        RequestCondition<?> activePatternsCondition = requestMappingInfo.getActivePatternsCondition();
        List<String> patternConditions;
        if (activePatternsCondition instanceof PathPatternsRequestCondition pathPatternsRequestCondition) {
            patternConditions = pathPatternsRequestCondition.getPatterns()
                    .stream()
                    .map(PathPattern::toString)
                    .toList();
        } else if (activePatternsCondition instanceof PatternsRequestCondition patternsRequestCondition) {
            Set<String> patterns = patternsRequestCondition.getPatterns();
            patternConditions = new ArrayList<>(patterns);
        } else {
            // will not happen
            throw new IllegalStateException();
        }

        String activePatternsConditionString;
        if (patternConditions.size() == 1) {
            activePatternsConditionString = patternConditions.iterator().next();
        } else {
            activePatternsConditionString = patternConditions.stream()
                    .sorted()
                    .collect(Collectors.joining(" || ", "[", "]"));
        }

        RequestMethodsRequestCondition methodsCondition = requestMappingInfo.getMethodsCondition();
        if (!methodsCondition.isEmpty()) {
            activePatternsConditionString += "  " + methodsCondition.getMethods();
        }

        return activePatternsConditionString;
    }

}
