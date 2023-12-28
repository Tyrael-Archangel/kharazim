package com.tyrael.kharazim.application.config.requestlog;

import com.tyrael.kharazim.application.system.domain.SystemRequestLog;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrentRequestLogHolder {

    private static final ThreadLocal<SystemRequestLog> SYSTEM_REQUEST_LOG_THREAD_LOCAL = new ThreadLocal<>();

    public static void prepare(SystemRequestLog systemRequestLog) {
        SYSTEM_REQUEST_LOG_THREAD_LOCAL.set(systemRequestLog);
    }

    public static SystemRequestLog get() {
        return SYSTEM_REQUEST_LOG_THREAD_LOCAL.get();
    }

    public static void clear() {
        SYSTEM_REQUEST_LOG_THREAD_LOCAL.remove();
    }

}
