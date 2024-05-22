package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
public class StringInternDemoTest {

    @Test
    public void stringIntern() {
        LocalDateTime endTime = LocalDateTime.now().plusHours(5);
        while (LocalDateTime.now().isBefore(endTime)) {
            String s = UUID.randomUUID().toString();
            String intern = s.intern();
            if (intern.length() > 100) {
                throw new RuntimeException("What happened.");
            }
        }
    }

}
