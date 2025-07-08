package com.tyrael.kharazim.gateway.filter;

import com.tyrael.kharazim.user.sdk.vo.ClientInfo;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Tyrael Archangel
 * @since 2025/7/8
 */
@Component
@SuppressWarnings("UastIncorrectHttpHeaderInspection")
public class RequestClientInfoResolver {

    private final String clientInfoAttributeKey = ClientInfo.class.getName();

    public ClientInfo resolveClientInfo(ServerWebExchange exchange) {

        Object clientInfoAttribute = exchange.getAttribute(clientInfoAttributeKey);

        if (clientInfoAttribute instanceof ClientInfo clientInfo) {
            return clientInfo;
        }

        ClientInfo clientInfo = this.getClientInfo(exchange);
        exchange.getAttributes().put(clientInfoAttributeKey, clientInfo);
        return clientInfo;
    }

    private ClientInfo getClientInfo(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String host = headers.getFirst("X-Forwarded-For");
        if (!StringUtils.hasText(host) || "unknown".equalsIgnoreCase(host)) {
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

}
