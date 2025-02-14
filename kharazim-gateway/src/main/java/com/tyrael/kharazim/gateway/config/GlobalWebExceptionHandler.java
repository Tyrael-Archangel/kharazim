package com.tyrael.kharazim.gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyrael.kharazim.lib.base.dto.DataResponse;
import com.tyrael.kharazim.lib.base.exception.ShouldNotHappenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.*;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Tyrael Archangel
 * @since 2025/2/12
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RequiredArgsConstructor
public class GlobalWebExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @NonNull
    @Override
    public Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {
        if (ex instanceof ResponseStatusException responseStatusException) {
            return writeError(exchange, responseStatusException.getStatusCode(), responseStatusException.getReason());
        } else {
            logError(exchange, ex);
            return writeError(exchange, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }

    private void logError(ServerWebExchange exchange, Throwable ex) {
        ServerHttpRequest request = exchange.getRequest();

        PathContainer pathContainer = request.getPath().pathWithinApplication();
        HttpMethod method = request.getMethod();
        String rawQuery = request.getURI().getRawQuery();
        String query = StringUtils.hasText(rawQuery) ? "?" + rawQuery : "";
        String msg = exchange.getLogPrefix() + " 500 Server Error for HTTP " + method + " \"" + pathContainer + query + "\"";

        log.error(msg, ex);
    }

    private Mono<Void> writeError(ServerWebExchange exchange, HttpStatusCode httpStatus, String reason) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders()
                .add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        DataResponse<Object> failResponse = DataResponse.fail(httpStatus.value(), reason);
        byte[] failResponseBytes;
        try {
            failResponseBytes = objectMapper.writeValueAsBytes(failResponse);
        } catch (JsonProcessingException e) {
            throw new ShouldNotHappenException();
        }
        DataBuffer dataBuffer = response.bufferFactory().wrap(failResponseBytes);
        return response.writeWith(Flux.just(dataBuffer));
    }

}
