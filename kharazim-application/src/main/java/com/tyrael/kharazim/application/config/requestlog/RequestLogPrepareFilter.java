package com.tyrael.kharazim.application.config.requestlog;

import cn.hutool.core.io.IoUtil;
import com.google.common.collect.Lists;
import com.tyrael.kharazim.application.base.auth.RequestPathMatcher;
import com.tyrael.kharazim.application.system.domain.SystemRequestLog;
import com.tyrael.kharazim.application.system.dto.requestlog.NameAndValue;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
@Slf4j
public class RequestLogPrepareFilter implements Filter {

    private final RequestPathMatcher ignoredUrlPatternMatcher;

    public RequestLogPrepareFilter(List<String> ignoreUrls) {
        this.ignoredUrlPatternMatcher = new RequestPathMatcher(ignoreUrls);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        CurrentRequestLogHolder.clear();

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if (this.needSaveRequestLog(httpServletRequest)) {

            byte[] contentBytes = null;
            MediaType mediaType = getMediaType(httpServletRequest);
            if (MediaType.APPLICATION_JSON.includes(mediaType)) {
                contentBytes = IoUtil.readBytes(request.getInputStream());
                httpServletRequest = new ContentCachingHttpServletRequestWrapper(httpServletRequest, contentBytes);
            }

            prepareLogRequest(httpServletRequest, contentBytes);
        }

        chain.doFilter(httpServletRequest, response);
    }

    private boolean needSaveRequestLog(HttpServletRequest httpServletRequest) {
        return !ignoredUrlPatternMatcher.matches(httpServletRequest);
    }

    private void prepareLogRequest(HttpServletRequest httpServletRequest, byte[] contentBytes) {

        Charset charset;
        try {
            charset = Charset.forName(httpServletRequest.getCharacterEncoding());
        } catch (UnsupportedCharsetException e) {
            charset = StandardCharsets.UTF_8;
        }

        try {
            SystemRequestLog systemRequestLog = new SystemRequestLog();

            String uri = URLDecoder.decode(httpServletRequest.getRequestURI(), charset);
            systemRequestLog.setUri(uri);
            systemRequestLog.setHttpMethod(httpServletRequest.getMethod());
            systemRequestLog.setRemoteAddr(httpServletRequest.getRemoteAddr());
            systemRequestLog.setForwardedFor(httpServletRequest.getHeader("X-Forwarded-For"));
            systemRequestLog.setRealIp(httpServletRequest.getHeader("X-Real-IP"));
            systemRequestLog.setRequestHeaders(this.requestHeaders(httpServletRequest));
            systemRequestLog.setRequestParams(this.requestParams(httpServletRequest));
            systemRequestLog.setRequestBody(this.requestBody(contentBytes, charset));
            systemRequestLog.setStartTime(LocalDateTime.now());

            CurrentRequestLogHolder.prepare(systemRequestLog);

        } catch (Exception e) {
            log.warn("prepare request log error: " + e.getMessage(), e);
        }
    }

    private String requestBody(byte[] contentBytes, Charset charset) {
        if (contentBytes == null) {
            return null;
        }

        return new String(contentBytes, charset);
    }

    private List<NameAndValue> requestHeaders(HttpServletRequest httpServletRequest) {
        List<NameAndValue> requestHeaders = Lists.newArrayList();
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String header = httpServletRequest.getHeader(headerName);
            requestHeaders.add(new NameAndValue(headerName, header));
        }
        return requestHeaders;
    }

    private List<NameAndValue> requestParams(HttpServletRequest httpServletRequest) {
        List<NameAndValue> requestParams = Lists.newArrayList();
        Enumeration<String> paramNames = httpServletRequest.getParameterNames();
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String param = httpServletRequest.getParameter(paramName);
            requestParams.add(new NameAndValue(paramName, param));
        }
        return requestParams;
    }

    private MediaType getMediaType(ServletRequest request) {
        String contentType = request.getContentType();
        if (contentType != null) {
            try {
                return MediaType.parseMediaType(contentType);
            } catch (Exception e) {
                log.warn("not supported contentType: " + contentType);
            }
        }
        return null;
    }

    public static class ContentCachingHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] contentBytes;

        public ContentCachingHttpServletRequestWrapper(HttpServletRequest request, byte[] contentBytes) {
            super(request);
            this.contentBytes = contentBytes;
        }

        @Override
        public ServletInputStream getInputStream() {
            return new RequestCachingInputStream(contentBytes);
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
        }

        private static class RequestCachingInputStream extends ServletInputStream {

            private final ByteArrayInputStream inputStream;

            public RequestCachingInputStream(byte[] bytes) {

                inputStream = new ByteArrayInputStream(bytes) {

                    @Override
                    public void mark(int readAheadLimit) {
                        mark = readAheadLimit;
                        pos = readAheadLimit;
                    }

                    @Override
                    public synchronized void reset() {
                        pos = 0;
                        mark = 0;
                    }
                };
            }

            @Override
            public int read() {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return inputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readlistener) {
                // not supported
            }

            @Override
            public synchronized void mark(int readAheadLimit) {
                inputStream.mark(readAheadLimit);
            }

            @Override
            public boolean markSupported() {
                return inputStream.markSupported();
            }

            @Override
            public synchronized void reset() {
                inputStream.reset();
            }
        }
    }

}
