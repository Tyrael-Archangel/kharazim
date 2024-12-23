package com.tyrael.kharazim.application.config.requestlog;

import com.tyrael.kharazim.application.system.domain.SystemRequestLog;
import com.tyrael.kharazim.application.system.dto.requestlog.SystemRequestLogConverter;
import com.tyrael.kharazim.application.system.service.SystemRequestLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/12/20
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(GlobalRequestLogConfig.class)
public class RequestLogRequestMappingPrepareInterceptor implements HandlerInterceptor, ApplicationRunner {

    private final BeanFactory beanFactory;
    private final SystemRequestLogService systemRequestLogService;
    private final SystemRequestLogConverter systemRequestLogConverter;
    private Map<HandlerMethod, RequestMappingInfo> handlerMethodMap;

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        try {
            determineMethodHandler(handler);
        } catch (Exception ignore) {
            // ignore
        }

        return true;
    }

    private void determineMethodHandler(Object handler) {
        SystemRequestLog systemRequestLog = CurrentRequestLogHolder.get();
        if (systemRequestLog == null) {
            return;
        }
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return;
        }

        RequestMappingInfo requestMappingInfo = handlerMethodMap.get(handlerMethod);
        if (requestMappingInfo == null) {
            return;
        }

        String endpoint = systemRequestLogConverter.requestMappingInfoEndpoint(requestMappingInfo);
        if (systemRequestLogService.isEndpointLogEnabled(endpoint)) {
            systemRequestLog.setEndpoint(endpoint);
        } else {
            CurrentRequestLogHolder.clear();
        }

    }

    @Override
    public void run(ApplicationArguments args) {
        RequestMappingHandlerMapping requestMappingHandlerMapping = beanFactory.getBean(
                "requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        this.handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getValue().createWithResolvedBean(),
                        Map.Entry::getKey,
                        (e1, e2) -> e1));
    }

}
