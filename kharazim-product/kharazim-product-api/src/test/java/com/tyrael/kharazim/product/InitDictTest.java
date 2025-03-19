package com.tyrael.kharazim.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
@SpringBootTest
public class InitDictTest {

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

    @Test
    public void initDict() {
    }

}