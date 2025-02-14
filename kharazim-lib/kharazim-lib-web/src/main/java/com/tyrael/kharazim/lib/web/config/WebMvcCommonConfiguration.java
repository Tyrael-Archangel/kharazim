package com.tyrael.kharazim.lib.web.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author Tyrael Archangel
 * @since 2025/2/11
 */
@Import({
        GlobalExceptionHandler.class,
        SystemErrorController.class,
        JacksonConfig.class,
        SystemGlobalWebConfig.class,
        WebMvcEnumConverterConfiguration.class
})
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@ConditionalOnMissingBean(DisableWebCommonConfiguration.class)
public class WebMvcCommonConfiguration {
}
