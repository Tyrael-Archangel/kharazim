package com.tyrael.kharazim.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tyrael Archangel
 * @since 2023/12/22
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "system.global")
public class SystemGlobalConfig {

    /**
     * 是否将异常轨迹打印给前端
     */
    private boolean enablePrintExceptionStackTrace = false;

    /**
     * 是否记录请求日志
     */
    private boolean enablePrintRequestLog = false;

    /**
     * 编码生成实现方式
     */
    private CodeGeneratorType codeGenerator = CodeGeneratorType.DATASOURCE;

    public enum CodeGeneratorType {

        /**
         * 使用redis实现编码生成
         */
        REDIS,
        /**
         * 使用数据库实现编码生成
         */
        DATASOURCE

    }

}
