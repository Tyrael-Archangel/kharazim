package com.tyrael.kharazim.application.base.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/24
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "system.global.auth")
public class AuthConfig {

    public static final String TOKEN_HEADER = "ACCESS-TOKEN";

    /**
     * 是否开启身份验证
     */
    private boolean enable = true;

    /**
     * 认证白名单路径
     */
    private List<String> whiteList = new ArrayList<>();

    /**
     * 是否允许多端登录
     */
    private boolean allowMultiLogin = false;

}
