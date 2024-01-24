package com.tyrael.kharazim.demo;

import java.util.*;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
public class CollectionRandomDemo {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {

        List<Integer> list = new ArrayList<>();
        Set<Integer> set = new LinkedHashSet<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
            set.add(i);
        }

        int loop = 10000000;
        long start1 = System.currentTimeMillis();
        Map<Integer, Integer> countMap1 = testRandom(loop, list);
        long end1 = System.currentTimeMillis();

        long start2 = System.currentTimeMillis();
        Map<Integer, Integer> countMap2 = testRandom(loop, set);
        long end2 = System.currentTimeMillis();

        System.out.println(countMap1);
        System.out.println(end1 - start1);
        System.out.println(countMap2);
        System.out.println(end2 - start2);
    }

    private static Map<Integer, Integer> testRandom(int loop, Collection<Integer> c) {
        Map<Integer, Integer> countMap = new TreeMap<>();
        for (int i = 0; i < loop; i++) {
            Integer random = random(c);
            Integer count = countMap.computeIfAbsent(random, k -> 0);
            countMap.put(random, count + 1);
        }
        return countMap;
    }

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
