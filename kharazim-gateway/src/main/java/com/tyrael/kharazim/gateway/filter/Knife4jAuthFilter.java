package com.tyrael.kharazim.gateway.filter;

import com.github.xiaoymin.knife4j.spring.gateway.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.spring.gateway.filter.basic.WebFluxSecurityBasicAuthFilter;
import com.tyrael.kharazim.authentication.PrincipalHeader;
import com.tyrael.kharazim.base.exception.UnauthorizedException;
import com.tyrael.kharazim.user.sdk.service.AuthServiceApi;
import com.tyrael.kharazim.user.sdk.vo.ClientInfo;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Tyrael Archangel
 * @since 2025/2/25
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@RequiredArgsConstructor
public class Knife4jAuthFilter extends WebFluxSecurityBasicAuthFilter {

    @DubboReference
    private AuthServiceApi authServiceApi;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        if (!super.match(exchange.getRequest().getURI().toString())) {
            return chain.filter(exchange);
        }

        return exchange.getSession().flatMap(webSession -> {

            String token = webSession.getAttribute(GlobalConstants.KNIFE4J_BASIC_AUTH_SESSION);
            if (token == null) {
                try {
                    token = tryAuth(exchange);
                } catch (UnauthorizedException e) {
                    writeForbiddenStatus(exchange);
                    return Mono.empty();
                }
                webSession.getAttributes().put(GlobalConstants.KNIFE4J_BASIC_AUTH_SESSION, token);
            }

            ServerHttpRequest extraRequest = exchange.getRequest()
                    .mutate()
                    .header(PrincipalHeader.TOKEN, token)
                    .build();
            ServerWebExchange mutatedExchange = exchange.mutate().request(extraRequest).build();
            ResponseCookie tokenCookie = ResponseCookie.from(PrincipalHeader.TOKEN, token).build();
            mutatedExchange.getResponse().addCookie(tokenCookie);
            return chain.filter(mutatedExchange);
        });
    }

    private String tryAuth(ServerWebExchange exchange) {
        String auth = exchange.getRequest().getHeaders().getFirst(GlobalConstants.AUTH_HEADER_NAME);
        if (!StringUtils.hasText(auth)) {
            throw new UnauthorizedException("Unauthorized");
        }

        String userNameAndPassword = decodeBase64(auth.substring(6));
        String[] userNameAndPasswordArray = userNameAndPassword.split(":");
        if (userNameAndPasswordArray.length != 2) {
            throw new UnauthorizedException("Unauthorized");
        }
        ClientInfo clientInfo = getClientInfo(exchange);
        return authServiceApi.login(userNameAndPasswordArray[0], userNameAndPasswordArray[1], clientInfo);
    }

    private ClientInfo getClientInfo(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String host = headers.getFirst("X-Forwarded-For");
        if (!StringUtils.hasText(host) || "unknown".equalsIgnoreCase(host)) {
            // noinspection UastIncorrectHttpHeaderInspection
            host = headers.getFirst("X-Real-IP");
        }
        if (!StringUtils.hasText(host) || "unknown".equalsIgnoreCase(host)) {
            host = Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                    .map(InetSocketAddress::getHostString)
                    .orElse("");
        }
        if (host.contains(",")) {
            host = host.split(",")[0];
        }

        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setHost(host);

        try {
            UserAgent userAgent = UserAgent.parseUserAgentString(headers.getFirst("User-Agent"));
            clientInfo.setOs(userAgent.getOperatingSystem().getName());
            clientInfo.setBrowser(userAgent.getBrowser().getName());
            clientInfo.setBrowserVersion(Objects.toString(userAgent.getBrowserVersion()));
        } catch (Exception ignore) {
            // ignore
        }

        return clientInfo;
    }

    private void writeForbiddenStatus(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Restricted Area\"");
    }

}
