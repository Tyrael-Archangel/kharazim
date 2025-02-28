package com.tyrael.kharazim.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyrael.kharazim.base.dto.Response;
import com.tyrael.kharazim.base.exception.ShouldNotHappenException;
import com.tyrael.kharazim.user.sdk.constant.UserHeader;
import com.tyrael.kharazim.user.sdk.exception.TokenInvalidException;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import com.tyrael.kharazim.user.sdk.service.AuthServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Tyrael Archangel
 * @since 2025/2/13
 */
@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter {

    public static final String AUTH_USER = "AUTH_USER";

    private final ObjectMapper objectMapper;
    private final AuthWhiteListChecker authWhiteListChecker;

    @DubboReference
    private AuthServiceApi authServiceApi;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        try {

            resolveUserHeader(exchange, this.getToken(exchange));
            return chain.filter(exchange);

        } catch (TokenInvalidException e) {

            if (isWhitelist(exchange)) {
                return chain.filter(exchange);
            } else {
                return writeUnauthorized(exchange);
            }
        }

    }

    private boolean isWhitelist(ServerWebExchange exchange) {
        PathContainer pathContainer = exchange.getRequest()
                .getPath()
                .pathWithinApplication();
        return authWhiteListChecker.isWhite(pathContainer);
    }

    private void resolveUserHeader(ServerWebExchange exchange, String token) throws TokenInvalidException {
        if (!StringUtils.hasText(token)) {
            throw new TokenInvalidException("Token is empty");
        }

        AuthUser authUser = authServiceApi.verifyToken(token);
        if (authUser == null) {
            throw new TokenInvalidException("Token validation failed");
        }

        exchange.getAttributes()
                .put(AUTH_USER, authUser);

        ServerHttpRequest extraRequest = exchange.getRequest()
                .mutate()
                .header(UserHeader.USER_ID, this.utf8Encode(authUser.getId().toString()))
                .header(UserHeader.USER_CODE, this.utf8Encode(authUser.getCode()))
                .header(UserHeader.USER_NAME, this.utf8Encode(authUser.getName()))
                .header(UserHeader.USER_NICKNAME, this.utf8Encode(authUser.getNickName()))
                .header(UserHeader.TOKEN, this.utf8Encode(token))
                .build();
        exchange.mutate().request(extraRequest).build();
    }

    private String getToken(ServerWebExchange exchange) {

        Map<String, String> queryParams = exchange.getRequest()
                .getQueryParams()
                .toSingleValueMap();
        String token = queryParams.get(UserHeader.TOKEN);
        if (!StringUtils.hasText(token)) {
            token = queryParams.entrySet()
                    .stream()
                    .filter(entry -> UserHeader.TOKEN.equalsIgnoreCase(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElse(null);
        }

        if (!StringUtils.hasText(token)) {
            token = exchange.getRequest()
                    .getHeaders()
                    .getFirst(UserHeader.TOKEN);
        }

        Map<String, HttpCookie> cookies = exchange.getRequest().getCookies().toSingleValueMap();
        if (!StringUtils.hasText(token)) {
            HttpCookie httpCookie = cookies.get(UserHeader.TOKEN);
            if (httpCookie != null) {
                token = httpCookie.getValue();
            }
        }
        if (!StringUtils.hasText(token)) {
            token = cookies.entrySet()
                    .stream()
                    .filter(entry -> UserHeader.TOKEN.equalsIgnoreCase(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .map(HttpCookie::getValue)
                    .orElse(null);
        }
        return token;
    }

    private Mono<Void> writeUnauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders()
                .add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        Response failResponse = Response.error(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        byte[] failResponseBytes;
        try {
            failResponseBytes = objectMapper.writeValueAsBytes(failResponse);
        } catch (JsonProcessingException e) {
            throw new ShouldNotHappenException();
        }
        DataBuffer dataBuffer = response.bufferFactory().wrap(failResponseBytes);

        return exchange.getSession()
                .flatMap(WebSession::invalidate)
                .then(response.writeWith(Flux.just(dataBuffer)));
    }

    private String utf8Encode(String value) {
        if (value == null) {
            return null;
        }
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}
