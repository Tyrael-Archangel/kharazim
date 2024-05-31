package com.tyrael.kharazim.common.util;

import cn.hutool.core.collection.CollUtil;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
public class CollectionUtils extends CollUtil {

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

}
