package com.tyrael.kharazim.common.util;

import cn.hutool.core.collection.CollUtil;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
public class CollectionUtils extends CollUtil {

    public static <T, K, V> Map<K, V> convertMap(Collection<T> from,
                                                 Function<T, K> keyFunc,
                                                 Function<T, V> valueFunc) {
        return convertMap(from, keyFunc, valueFunc, (v1, v2) -> v1);
    }

    public static <T, K, V> Map<K, V> convertMap(Collection<T> from,
                                                 Function<T, K> keyFunc,
                                                 Function<T, V> valueFunc,
                                                 BinaryOperator<V> mergeFunction) {
        return convertMap(from, keyFunc, valueFunc, mergeFunction, LinkedHashMap::new);
    }

    public static <T, K, V> Map<K, V> convertMap(Collection<T> from,
                                                 Function<T, K> keyFunc,
                                                 Function<T, V> valueFunc,
                                                 BinaryOperator<V> mergeFunction,
                                                 Supplier<? extends Map<K, V>> supplier) {
        if (from == null || from.isEmpty()) {
            return supplier.get();
        }
        return from.stream()
                .collect(Collectors.toMap(keyFunc, valueFunc, mergeFunction, supplier));
    }

}
