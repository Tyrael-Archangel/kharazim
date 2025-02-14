package com.tyrael.kharazim.lib.web.config.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Tyrael Archangel
 * @since 2025/2/11
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(com.tyrael.kharazim.lib.web.config.DisableWebCommonConfiguration.class)
public @interface DisableWebCommonConfiguration {
}
