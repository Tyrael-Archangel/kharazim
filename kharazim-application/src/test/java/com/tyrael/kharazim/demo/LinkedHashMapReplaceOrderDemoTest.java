package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

/**
 * @author Tyrael Archangel
 * @since 2025/7/14
 */
public class LinkedHashMapReplaceOrderDemoTest {

    @Test
    public void linkedHashMapReplaceOrder() {

        LinkedHashMap<Long, String> map = new LinkedHashMap<>(16, 0.75f, true);
        map.put(1L, "A");
        map.put(2L, "B");
        map.put(3L, "C");
        System.out.println("Before replacement: " + map);

        // Replace the value for key 2
        map.put(2L, "D");
        System.out.println("After replacement: " + map);
    }

    @Test
    public void linkedHashMapReplaceOrderWithNewKey() {

        LinkedHashMap<Long, String> map = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(java.util.Map.Entry<Long, String> eldest) {
                return size() > 3;
            }
        };
        map.put(2L, "B");
        map.put(4L, "D");
        map.put(3L, "C");
        map.put(1L, "A");
        System.out.println(map);
        map.put(1L, "A1");
        map.get(3L);
        System.out.println(map);
    }

}
