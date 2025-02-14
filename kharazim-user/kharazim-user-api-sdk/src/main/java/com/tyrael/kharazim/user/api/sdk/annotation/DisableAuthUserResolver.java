package com.tyrael.kharazim.user.api.sdk.annotation;

import com.tyrael.kharazim.user.api.sdk.config.DisableAuthUserResolverConfiguration;
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
@Import(DisableAuthUserResolverConfiguration.class)
public @interface DisableAuthUserResolver {
}
