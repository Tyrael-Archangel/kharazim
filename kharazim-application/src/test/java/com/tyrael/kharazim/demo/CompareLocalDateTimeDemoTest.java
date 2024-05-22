package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
public class CompareLocalDateTimeDemoTest {

    @Test
    public void compareLocalDateTime() {

        LocalDateTime time = LocalDateTime.now().plusHours(5);
        System.out.println(LocalDateTime.now().isAfter(time));

    }

}
