package com.tyrael.kharazim.gateway.filter;

import com.tyrael.kharazim.user.sdk.exception.TokenInvalidException;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import com.tyrael.kharazim.user.sdk.service.AuthServiceApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

import static com.tyrael.kharazim.authentication.PrincipalHeader.TOKEN;

/**
 * @author Tyrael Archangel
 * @since 2025/3/18
 */
@Component
public class AuthUserResolver {

    private final String authUserAttributeKey = AuthUser.class.getName();

    @DubboReference
    private AuthServiceApi authServiceApi;

    public AuthUser resolveUser(ServerWebExchange exchange) {

        Object authUserAttribute = exchange.getAttribute(authUserAttributeKey);

        if (authUserAttribute instanceof AuthUser authUser) {
            return authUser;
        }
        if (authUserAttribute instanceof TokenInvalidException ex) {
            throw ex;
        }

        try {

            AuthUser authUser = this.doResolveUser(exchange);
            exchange.getAttributes().put(authUserAttributeKey, authUser);
            return authUser;

        } catch (TokenInvalidException e) {

            exchange.getAttributes().put(authUserAttributeKey, e);
            throw e;
        }
    }

    private AuthUser doResolveUser(ServerWebExchange exchange) {
        String token = this.getToken(exchange);
        if (!StringUtils.hasText(token)) {
            throw new TokenInvalidException("Token is empty");
        }

        AuthUser authUser = authServiceApi.verifyToken(token);
        if (authUser == null) {
            throw new TokenInvalidException("Token validation failed");
        }

        return authUser;
    }

    private String getToken(ServerWebExchange exchange) {

        String tokenKey = TOKEN;

        String token = exchange.getRequest()
                .getHeaders()
                .getFirst(tokenKey);

        if (!StringUtils.hasText(token)) {
            Map<String, String> queryParams = exchange.getRequest()
                    .getQueryParams()
                    .toSingleValueMap();
            token = queryParams.get(tokenKey);
            if (!StringUtils.hasText(token)) {
                token = queryParams.entrySet()
                        .stream()
                        .filter(entry -> tokenKey.equalsIgnoreCase(entry.getKey()))
                        .map(Map.Entry::getValue)
                        .findFirst()
                        .orElse(null);
            }
        }

        if (!StringUtils.hasText(token)) {
            Map<String, HttpCookie> cookies = exchange.getRequest().getCookies().toSingleValueMap();
            HttpCookie httpCookie = cookies.get(tokenKey);
            if (httpCookie != null) {
                token = httpCookie.getValue();
            }
            if (!StringUtils.hasText(token)) {
                token = cookies.entrySet()
                        .stream()
                        .filter(entry -> tokenKey.equalsIgnoreCase(entry.getKey()))
                        .map(Map.Entry::getValue)
                        .findFirst()
                        .map(HttpCookie::getValue)
                        .orElse(null);
            }
        }

        return token;
    }

}
