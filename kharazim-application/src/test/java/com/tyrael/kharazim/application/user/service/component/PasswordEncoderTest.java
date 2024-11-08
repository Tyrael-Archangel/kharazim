package com.tyrael.kharazim.application.user.service.component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * @author Tyrael Archangel
 * @since 2024/11/8
 */
class PasswordEncoderTest {

    @Test
    void encodeAndMatch() {
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        for (int i = 0; i < 100; i++) {
            String rawPassword = UUID.randomUUID().toString();
            String encode = passwordEncoder.encode(rawPassword);
            boolean matches = passwordEncoder.matches(rawPassword, encode);
            Assertions.assertTrue(matches);
        }
    }

}
