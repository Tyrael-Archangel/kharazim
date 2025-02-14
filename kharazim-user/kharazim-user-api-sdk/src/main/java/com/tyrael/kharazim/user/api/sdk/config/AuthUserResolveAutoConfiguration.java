package com.tyrael.kharazim.user.api.sdk.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author Tyrael Archangel
 * @since 2025/2/11
 */
@Import({
        AuthUserMethodArgumentResolver.class,
        AuthUserResolveMvcConfigurer.class,
        AuthUserResolverInterceptor.class
})
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@ConditionalOnMissingBean(DisableAuthUserResolverConfiguration.class)
public class AuthUserResolveAutoConfiguration {
}
