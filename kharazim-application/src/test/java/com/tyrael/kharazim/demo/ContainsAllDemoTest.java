package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2025/1/21
 */
public class ContainsAllDemoTest {

    @Test
    public void containsAll() {
        Set<String> all = Set.of("a", "b", "c");
        Set<String> expected = Set.of("a", "b");
        System.out.println(expected.containsAll(all));
        System.out.println(all.containsAll(expected));
    }

}
