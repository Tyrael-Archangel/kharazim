package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author Tyrael Archangel
 * @since 2024/3/12
 */
public class RandomRateDemoTest {

    @Test
    public void randomRate() {

        Map<Integer, Integer> countMap = new TreeMap<>();
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int value = random.nextInt(10);
            countMap.merge(value, 1, Integer::sum);
        }

        System.out.println(countMap);
    }

}
