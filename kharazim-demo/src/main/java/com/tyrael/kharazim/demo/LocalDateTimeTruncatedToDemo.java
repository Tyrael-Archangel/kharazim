package com.tyrael.kharazim.demo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Tyrael Archangel
 * @since 2024/2/4
 */
public class LocalDateTimeTruncatedToDemo {

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = now.truncatedTo(ChronoUnit.SECONDS);

        System.out.println(now);
        System.out.println(dateTime);
    }

}
