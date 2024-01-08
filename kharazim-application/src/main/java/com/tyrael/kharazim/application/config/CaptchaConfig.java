package com.tyrael.kharazim.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "captcha")
public class CaptchaConfig {

    /**
     * 验证码获取间隔，默认5分钟
     */
    private Integer intervalMinutes = 5;

    /**
     * 验证码长度，默认6位
     */
    private Integer captchaLength = 6;

    /**
     * 验证码缓存前缀
     */
    private String captchaCodePrefix = "SMS_CAPTCHA_CODE";
}
