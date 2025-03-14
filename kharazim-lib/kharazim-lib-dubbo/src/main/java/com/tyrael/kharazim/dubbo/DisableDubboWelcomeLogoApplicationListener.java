package com.tyrael.kharazim.dubbo;

import org.apache.dubbo.spring.boot.context.event.WelcomeLogoApplicationListener;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Tyrael Archangel
 * @since 2025/3/14
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class DisableDubboWelcomeLogoApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private final AtomicBoolean thisProcessed = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(@NonNull ApplicationEnvironmentPreparedEvent event) {
        if (thisProcessed.get()) {
            return;
        }
        try {
            Field processedField = WelcomeLogoApplicationListener.class.getDeclaredField("processed");
            processedField.setAccessible(true);
            AtomicBoolean processed = (AtomicBoolean) processedField.get(null);
            processed.set(true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // ignore
        }
        thisProcessed.compareAndSet(false, true);
    }

}
