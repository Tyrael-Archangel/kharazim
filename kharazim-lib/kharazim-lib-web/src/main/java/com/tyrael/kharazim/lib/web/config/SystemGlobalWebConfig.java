package com.tyrael.kharazim.lib.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tyrael Archangel
 * @since 2023/12/22
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "system.global.web")
public class SystemGlobalWebConfig {

    /**
     * 是否将异常轨迹打印给前端
     */
    private boolean enablePrintExceptionStackTrace = false;

}
