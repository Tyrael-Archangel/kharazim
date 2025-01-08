package com.tyrael.kharazim.application.base.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "system.global.auth.token")
public class AuthTokenConfig {

    /**
     * token过期时间
     */
    private long tokenExpireSeconds = 3600L;

    /**
     * token缓存前缀
     */
    private String tokenCachePrefix = "AUTH_TOKEN";

    /**
     * 用户token缓存前缀
     */
    private String userTokenCachePrefix = "AUTH_TOKEN_USER";

    public Duration getTokenExpire() {
        return Duration.ofSeconds(tokenExpireSeconds);
    }

}
