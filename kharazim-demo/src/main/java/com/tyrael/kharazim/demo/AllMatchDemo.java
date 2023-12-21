package com.tyrael.kharazim.demo;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
public class AllMatchDemo {

    public static void main(String[] args) {
        List<String> list = List.of("abc", "bcd", "xyz");
        boolean allMatch = list.stream()
                .allMatch(e -> e.length() > 1);
        System.out.println(allMatch);
    }

}
