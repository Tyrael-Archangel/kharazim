package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Tyrael Archangel
 * @since 2024/2/4
 */
public class LocalDateTimeTruncatedToDemoTest {

    @Test
    public void localDateTimeTruncatedTo() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = now.truncatedTo(ChronoUnit.SECONDS);

        System.out.println(now);
        System.out.println(dateTime);
    }

}
