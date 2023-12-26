package com.tyrael.kharazim.web.config;

import com.tyrael.kharazim.application.base.auth.AuthConfig;
import com.tyrael.kharazim.application.base.auth.CurrentUserMethodArgumentResolver;
import com.tyrael.kharazim.application.base.auth.GlobalAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver;
    private final GlobalAuthInterceptor globalAuthInterceptor;
    private final AuthConfig authConfig;

    @Value("${server.error.path:${error.path:/error}}")
    private String errorPath;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserMethodArgumentResolver);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new EnumConverterFactory());
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {

        if (authConfig.isEnable()) {

            String[] knife4jResources = {
                    "/swagger-resources/**",
                    "/doc.html",
                    "/webjars/**",
                    "/*/api-docs/**"
            };

            registry.addInterceptor(globalAuthInterceptor)
                    .excludePathPatterns(errorPath)
                    .excludePathPatterns("/favicon.ico")
                    .excludePathPatterns("/")
                    .excludePathPatterns(knife4jResources)
                    .order(Ordered.HIGHEST_PRECEDENCE);
        }
    }

}
