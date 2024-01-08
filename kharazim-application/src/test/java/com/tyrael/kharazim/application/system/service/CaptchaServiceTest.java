package com.tyrael.kharazim.application.system.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
@SpringBootTest
class CaptchaServiceTest {

    @Autowired
    private CaptchaService captchaService;

    @Test
    void sendSmsCaptcha() {
        String serialCode = captchaService.sendSmsCaptcha("15011112222");
        System.out.println(serialCode);
    }

    @Test
    void verifySmsCaptcha() {
        String captcha = "412147";
        String serialCode = "rjDsAYEr";
        captchaService.verifySmsCaptcha(captcha, serialCode);
    }

}