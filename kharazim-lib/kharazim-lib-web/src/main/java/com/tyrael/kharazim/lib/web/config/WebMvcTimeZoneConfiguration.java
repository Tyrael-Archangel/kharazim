package com.tyrael.kharazim.lib.web.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.TimeZone;

/**
 * @author Tyrael Archangel
 * @since 2025/6/20
 */
@Slf4j
public class WebMvcTimeZoneConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(new TimeZoneResolveInterceptor());
    }

    public static class TimeZoneResolveInterceptor implements WebRequestInterceptor {

        @Override
        public void preHandle(WebRequest request) {
            String timeZoneHeader = request.getHeader("Time-Zone");
            if (StringUtils.isNotBlank(timeZoneHeader)) {
                try {
                    LocaleContextHolder.setTimeZone(TimeZone.getTimeZone(timeZoneHeader));
                } catch (Exception e) {
                    log.warn("unsupported time-zone header: {}", timeZoneHeader);
                }
            } else {
                LocaleContextHolder.setTimeZone(TimeZone.getDefault());
            }
        }

        @Override
        public void postHandle(@NonNull WebRequest webRequest, @Nullable ModelMap model) {
            // do nothing
        }

        @Override
        public void afterCompletion(@NonNull WebRequest webRequest, @Nullable Exception ex) {
            // do nothing
        }
    }

}
