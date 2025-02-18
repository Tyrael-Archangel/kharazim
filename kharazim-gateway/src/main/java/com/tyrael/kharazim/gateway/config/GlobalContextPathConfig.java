package com.tyrael.kharazim.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.WebFilter;

/**
 * @author Tyrael Archangel
 * @since 2025/2/18
 */
@Configuration
public class GlobalContextPathConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public WebFilter globalContextPathFilter(@Value("${spring.cloud.gateway.context-path:/kharazim-api}") String contextPath) {

        if (contextPath == null || contextPath.isBlank()) {
            return (exchange, chain) -> chain.filter(exchange);
        }

        String finalContextPath = contextPath.startsWith("/")
                ? contextPath
                : "/" + contextPath;
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getRawPath();
            if (path.startsWith(finalContextPath)) {
                String newPath = path.substring(finalContextPath.length());
                if (newPath.isBlank()) {
                    newPath = "/";
                }
                exchange = exchange.mutate()
                        .request(exchange.getRequest().mutate().path(newPath).build())
                        .build();
            }
            return chain.filter(exchange);
        };
    }

}
