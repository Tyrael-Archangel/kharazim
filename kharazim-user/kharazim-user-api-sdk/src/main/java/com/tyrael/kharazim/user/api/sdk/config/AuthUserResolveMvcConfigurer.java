package com.tyrael.kharazim.user.api.sdk.config;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/2/11
 */
public class AuthUserResolveMvcConfigurer implements WebMvcConfigurer {

    private final AuthUserMethodArgumentResolver authUserMethodArgumentResolver;
    private final AuthUserResolverInterceptor authUserResolverInterceptor;

    public AuthUserResolveMvcConfigurer(AuthUserMethodArgumentResolver authUserMethodArgumentResolver,
                                        AuthUserResolverInterceptor authUserResolverInterceptor) {
        this.authUserMethodArgumentResolver = authUserMethodArgumentResolver;
        this.authUserResolverInterceptor = authUserResolverInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserMethodArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(authUserResolverInterceptor);
    }

}
