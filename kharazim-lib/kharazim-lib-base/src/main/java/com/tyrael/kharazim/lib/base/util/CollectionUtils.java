package com.tyrael.kharazim.lib.base.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionUtils {

    private static final Random RANDOM = ThreadLocalRandom.current();

    public static <T> T random(Collection<T> c) {
        if (isEmpty(c)) {
            throw new IllegalArgumentException("Collection is empty");
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

    public static <T> List<T> randomSubList(Collection<T> c, int percent) {
        if (percent < 1 || percent > 100) {
            throw new IllegalArgumentException("percent must be between 1 and 100");
        }
        List<T> subList = new ArrayList<>();
        if (isEmpty(c)) {
            return subList;
        }
        for (T t : c) {
            if (RANDOM.nextInt(100) < percent) {
                subList.add(t);
            }
        }
        return subList;
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

    public static <T> Set<T> union(Set<T> set1, Set<T> set2) {
        HashSet<T> all = new HashSet<>();
        all.addAll(set1);
        all.addAll(set2);
        return all;
    }

}
