package com.tyrael.kharazim.application.config.requestlog;

import com.google.common.collect.Lists;
import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUserHolder;
import com.tyrael.kharazim.application.system.domain.SystemRequestLog;
import com.tyrael.kharazim.application.system.dto.requestlog.NameAndValue;
import com.tyrael.kharazim.application.system.service.SystemRequestLogService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@ConditionalOnBean(GlobalRequestLogConfig.class)
public class RequestLogWriteAdvice implements ResponseBodyAdvice<Object> {

    private final SystemRequestLogService systemRequestLogService;
    private final List<HttpMessageConverter<?>> httpMessageConverters;

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedMediaType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {

        writeRequestLog(body, selectedMediaType, selectedConverterType, response);
        return body;
    }

    private void writeRequestLog(Object body,
                                 MediaType selectedMediaType,
                                 Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                 ServerHttpResponse response) {

        try {

            SystemRequestLog systemRequestLog = CurrentRequestLogHolder.get();
            if (systemRequestLog == null) {
                return;
            }

            AuthUser currentUser = CurrentUserHolder.getCurrentUser();
            systemRequestLog.setUserName(currentUser == null ? null : currentUser.getName());
            systemRequestLog.setResponseHeaders(this.responseHeaders(response));
            systemRequestLog.setResponseBody(this.responseBody(body, selectedMediaType, selectedConverterType));
            systemRequestLog.setResponseStatus(this.responseStatus(response));
            systemRequestLog.setEndTime(LocalDateTime.now());

            systemRequestLogService.save(systemRequestLog);

        } catch (Exception e) {
            log.error("save system request log error: {}", e.getMessage(), e);
        } finally {
            CurrentRequestLogHolder.clear();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private String responseBody(Object body,
                                MediaType selectedMediaType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType) {

        if (body == null) {
            return null;
        }

        // 跟spring序列化方式保持一致，不直接用FastJson或者ObjectMapper转
        for (HttpMessageConverter<?> converter : this.httpMessageConverters) {

            if (converter.getClass().equals(selectedConverterType)) {
                OutputMessageHelper outputMessage = new OutputMessageHelper(selectedMediaType);
                try {
                    ((HttpMessageConverter) converter).write(body, selectedMediaType, outputMessage);
                } catch (IOException e) {
                    log.warn("write response message error: {}", e.getMessage(), e);
                    return null;
                }
                return outputMessage.getContent();
            }
        }

        return null;
    }

    private int responseStatus(ServerHttpResponse response) {
        if (response instanceof ServletServerHttpResponse servletServerHttpResponse) {
            return servletServerHttpResponse.getServletResponse().getStatus();
        }
        return 200;
    }

    private List<NameAndValue> responseHeaders(ServerHttpResponse response) {

        List<NameAndValue> requestHeaders = Lists.newArrayList();
        if (response instanceof ServletServerHttpResponse servletServerHttpResponse) {
            HttpServletResponse servletResponse = servletServerHttpResponse.getServletResponse();
            Collection<String> headerNames = servletResponse.getHeaderNames();
            if (headerNames != null) {
                for (String headerName : headerNames) {
                    String headerValue = servletResponse.getHeader(headerName);
                    requestHeaders.add(new NameAndValue(headerName, headerValue));
                }
            }
        } else {

            HttpHeaders headers = response.getHeaders();
            headers.forEach((headerName, headerValues) -> {
                String headerValue = headerValues == null ? null : String.join("; ", headerValues);
                requestHeaders.add(new NameAndValue(headerName, headerValue));
            });
        }

        return requestHeaders;
    }

    private static class OutputMessageHelper implements HttpOutputMessage {

        private final ByteArrayOutputStream outputStream;
        private final HttpHeaders httpHeaders;

        public OutputMessageHelper(MediaType selectedMediaType) {
            this.outputStream = new ByteArrayOutputStream();
            this.httpHeaders = new HttpHeaders();
            this.httpHeaders.add(HttpHeaders.CONTENT_TYPE, selectedMediaType.toString());
        }

        @Override
        @NonNull
        public OutputStream getBody() {
            return outputStream;
        }

        @Override
        @NonNull
        public HttpHeaders getHeaders() {
            return httpHeaders;
        }

        public String getContent() {
            return outputStream.toString(StandardCharsets.UTF_8);
        }

    }

}
