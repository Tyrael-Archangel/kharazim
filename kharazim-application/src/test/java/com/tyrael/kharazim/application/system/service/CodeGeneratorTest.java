package com.tyrael.kharazim.application.system.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.tyrael.kharazim.application.config.BusinessCodeConstants.SYSTEM_TEST;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@SpringBootTest(properties = {
        "system.global.code-generator=DATASOURCE"
})
class CodeGeneratorTest {

    @Resource
    private CodeGenerator codeGenerator;

    @Test
    void next() {
        for (int i = 0; i < 10; i++) {
            String next = codeGenerator.next(SYSTEM_TEST);
            System.out.println(next);
        }
    }

    @Test
    void dailyNext() {
        for (int i = 0; i < 10; i++) {
            String next = codeGenerator.dailyNext(SYSTEM_TEST);
            System.out.println(next);
        }
    }

    @Test
    void dailyTimeNext() {
        for (int i = 0; i < 10; i++) {
            String next = codeGenerator.dailyTimeNext(SYSTEM_TEST);
            System.out.println(next);
        }
    }

}

