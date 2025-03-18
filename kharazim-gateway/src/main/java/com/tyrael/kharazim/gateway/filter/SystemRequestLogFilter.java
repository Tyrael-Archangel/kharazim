package com.tyrael.kharazim.gateway.filter;

import com.tyrael.kharazim.base.exception.ShouldNotHappenException;
import com.tyrael.kharazim.basicdata.sdk.model.SystemRequestLogVO;
import com.tyrael.kharazim.basicdata.sdk.service.SystemRequestLogServiceApi;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Tyrael Archangel
 * @since 2025/2/28
 */
@Slf4j
@SuppressWarnings("UastIncorrectHttpHeaderInspection")
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class SystemRequestLogFilter implements GlobalFilter {

    private final AuthUserResolver authUserResolver;
    private final SystemRequestLogPathMather requestLogPathMather;
    private final LogChannel logChannel = new LogChannel();

    @DubboReference
    private SystemRequestLogServiceApi systemRequestLogServiceApi;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        PathContainer pathContainer = exchange.getRequest()
                .getPath()
                .pathWithinApplication();
        if (requestLogPathMather.ignoreRequestLog(pathContainer)) {
            return chain.filter(exchange);
        }

        return exchange.getRequest()
                .getBody()
                .collectList() // 收集所有的请求体数据
                .flatMap(requestBody -> {

                    byte[] requestBodyBytes = mergeBytes(requestBody);
                    SystemRequestLogVO logVO = prepareLogRequest(exchange, requestBodyBytes);

                    DataBuffer requestBodyBuffer = exchange.getResponse().bufferFactory().wrap(requestBodyBytes);
                    ServerWebExchange build = exchange.mutate()
                            .request(new BodyCacheServerHttpRequestDecorator(exchange.getRequest(), requestBodyBuffer))
                            .response(new LogResolveServerHttpResponseDecorator(exchange.getResponse(), logVO))
                            .build();
                    return chain.filter(build);
                });

    }

    private SystemRequestLogVO prepareLogRequest(ServerWebExchange exchange, byte[] bodyBytes) {

        AuthUser authUser;
        try {
            authUser = authUserResolver.resolveUser(exchange);
        } catch (Exception ignore) {
            // ignore
            authUser = null;
        }

        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String uri = URLDecoder.decode(request.getURI().toString(), StandardCharsets.UTF_8);
        String remoteAddr = Optional.ofNullable(request.getRemoteAddress())
                .map(InetSocketAddress::getHostString)
                .orElse(null);

        return SystemRequestLogVO.builder()
                .uri(uri)
                .remoteAddr(remoteAddr)
                .forwardedFor(headers.getFirst("X-Forwarded-For"))
                .realIp(headers.getFirst("X-Real-IP"))
                .httpMethod(request.getMethod().toString())
                .requestHeaders(this.nameValues(headers))
                .requestParams(this.nameValues(request.getQueryParams()))
                .requestBody(new String(bodyBytes, StandardCharsets.UTF_8))
                .userCode(authUser == null ? null : authUser.getCode())
                .userName(authUser == null ? null : authUser.getNickName())
                .startTime(LocalDateTime.now())
                .build();
    }

    private List<SystemRequestLogVO.NameAndValue> nameValues(MultiValueMap<String, String> headers) {
        List<SystemRequestLogVO.NameAndValue> nameAndValues = new ArrayList<>();
        for (Map.Entry<String, List<String>> header : headers.entrySet()) {
            String headerKey = header.getKey();
            List<String> headerValues = header.getValue();
            for (String headerValue : headerValues) {
                SystemRequestLogVO.NameAndValue nameAndValue = new SystemRequestLogVO.NameAndValue();
                nameAndValue.setName(headerKey);
                nameAndValue.setValue(headerValue);
                nameAndValues.add(nameAndValue);
            }
        }
        return nameAndValues;
    }

    private byte[] mergeBytes(List<? extends DataBuffer> dataBuffers) {
        int size = dataBuffers.stream()
                .map(DataBuffer::readableByteCount)
                .reduce(0, Integer::sum);
        ByteBuffer allocate = ByteBuffer.allocate(size);
        for (DataBuffer dataBuffer : dataBuffers) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(dataBuffer.readableByteCount());
            dataBuffer.toByteBuffer(byteBuffer);
            allocate.put(byteBuffer);
        }

        return allocate.array();
    }

    private static class BodyCacheServerHttpRequestDecorator extends ServerHttpRequestDecorator {

        private final DataBuffer dataBuffer;

        BodyCacheServerHttpRequestDecorator(ServerHttpRequest delegate, DataBuffer dataBuffer) {
            super(delegate);
            this.dataBuffer = dataBuffer;
        }

        @Override
        @NonNull
        public Flux<DataBuffer> getBody() {
            return Flux.just(dataBuffer);
        }

    }

    private class LogResolveServerHttpResponseDecorator extends ServerHttpResponseDecorator {

        private final SystemRequestLogVO logVO;

        public LogResolveServerHttpResponseDecorator(ServerHttpResponse serverHttpResponse, SystemRequestLogVO logVO) {
            super(serverHttpResponse);
            this.logVO = logVO;
        }

        @Override
        @NonNull
        public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
            String contentType = this.getHeaders().getFirst("Content-Type");
            if (StringUtils.hasText(contentType)
                    && contentType.toLowerCase().contains(MediaType.APPLICATION_JSON_VALUE)
                    && body instanceof Flux<? extends DataBuffer> fluxBody) {
                return fluxBody.collectList().flatMap(dataBuffers -> {

                    byte[] responseBytes = mergeBytes(dataBuffers);
                    saveRequestLog(responseBytes);

                    return super.writeWith(Flux.just(this.bufferFactory().wrap(responseBytes)));
                });
            }
            return super.writeWith(body);
        }

        private void saveRequestLog(byte[] responseBytes) {

            Integer responseStatus = Optional.ofNullable(this.getStatusCode())
                    .map(HttpStatusCode::value)
                    .orElse(null);

            logVO.setResponseHeaders(nameValues(this.getHeaders()));
            logVO.setResponseStatus(responseStatus);
            logVO.setResponseBody(new String(responseBytes, StandardCharsets.UTF_8));
            logVO.setEndTime(LocalDateTime.now());

            logChannel.save(logVO);
        }
    }

    /**
     * log channel
     */
    private class LogChannel {

        private final LinkedBlockingQueue<SystemRequestLogVO> logQueue;

        LogChannel() {
            logQueue = new LinkedBlockingQueue<>();
            new Thread(this::sendLog, "T-log-channel").start();
        }

        void save(SystemRequestLogVO logVO) {
            logQueue.add(logVO);
        }

        private void sendLog() {
            while (true) {
                SystemRequestLogVO logVO;
                try {
                    logVO = logQueue.take();
                } catch (InterruptedException e) {
                    log.error("take log form queue failed: {}", e.getMessage(), e);
                    throw new ShouldNotHappenException(e);
                }
                systemRequestLogServiceApi.save(logVO);
            }
        }

    }

}
