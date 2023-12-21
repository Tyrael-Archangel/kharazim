package com.tyrael.kharazim.demo;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
public class CompareLocalDateTimeDemo {

    public static void main(String[] args) {

        LocalDateTime time = LocalDateTime.now().plusHours(5);
        System.out.println(LocalDateTime.now().isAfter(time));

    }

}
