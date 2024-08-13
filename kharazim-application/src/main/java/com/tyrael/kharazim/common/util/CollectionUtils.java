package com.tyrael.kharazim.common.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionUtils {

    private static final Random RANDOM = new Random();

    public static <T> T random(Collection<T> c) {
        if (c == null || c.isEmpty()) {
            return null;
        }
        int size = c.size();
        int valueIndex = RANDOM.nextInt(size);
        if (c instanceof List<T> list && list instanceof RandomAccess) {
            return list.get(valueIndex);
        }
        for (T next : c) {
            if (valueIndex-- <= 0) {
                return next;
            }
        }
        throw new IllegalStateException("Will not happen");
    }

    public static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static <E> Collection<E> safeCollection(Collection<E> collection) {
        return isEmpty(collection) ? new ArrayList<>() : collection;
    }

    public static <E> List<E> safeList(List<E> list) {
        return isEmpty(list) ? new ArrayList<>() : list;
    }

    public static <E> Stream<E> safeStream(Collection<E> collection) {
        return isEmpty(collection) ? Stream.empty() : collection.stream();
    }

    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(final Map<?, ?> map) {
        return !isEmpty(map);
    }

}
