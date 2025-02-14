package com.tyrael.kharazim.dubbo.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * @author Tyrael Archangel
 * @since 2025/2/13
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SourceApplicationHolder {

    private static final ThreadLocal<SourceDubboApplication> SOURCE_APPLICATION_THREAD_LOCAL = new ThreadLocal<>();

    public static @NonNull SourceDubboApplication getSourceApplication() {
        SourceDubboApplication sourceDubboApplication = SOURCE_APPLICATION_THREAD_LOCAL.get();
        return sourceDubboApplication == null ? SourceDubboApplication.UNKNOWN_APPLICATION : sourceDubboApplication;
    }

    public static void setSourceApplication(SourceDubboApplication sourceDubboApplication) {
        SOURCE_APPLICATION_THREAD_LOCAL.set(sourceDubboApplication);
    }

    public static void remove() {
        SOURCE_APPLICATION_THREAD_LOCAL.remove();
    }
}
