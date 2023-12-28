package com.tyrael.kharazim.application.config.requestlog;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
@Data
@Configuration
@ConditionalOnProperty(value = "system.global.enable-print-request-log", havingValue = "true")
@ConfigurationProperties(prefix = "system.global.request-log")
public class GlobalRequestLogConfig {

    private List<String> ignoreUrls = new ArrayList<>();

    @Bean
    public FilterRegistrationBean<RequestLogPrepareFilter> contentCachingRegistrationBean() {
        FilterRegistrationBean<RequestLogPrepareFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestLogPrepareFilter(ignoreUrls));
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }

}
