package com.tyrael.kharazim.gateway.filter;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/7/7
 */
public final class FilterOrders {

    private static final int HIGHEST = 1000;

    private static final List<Class<?>> FILTER_ORDER_LIST = List.of
            (
                    RateLimiterFilter.class,
                    Knife4jAuthFilter.class,
                    SystemRequestLogFilter.class,
                    AuthFilter.class
            );

    static int getOrder(Class<?> filterClass) {
        return FILTER_ORDER_LIST.indexOf(filterClass) + HIGHEST;
    }

}
