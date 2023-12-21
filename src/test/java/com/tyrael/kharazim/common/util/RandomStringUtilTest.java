package com.tyrael.kharazim.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
class RandomStringUtilTest {

    @Test
    void make() {
        String random = RandomStringUtil.make();
        Assertions.assertNotNull(random);
        System.out.println(random);
    }

    @Test
    void makeWithLength() {
        String random = RandomStringUtil.make(32);
        Assertions.assertNotNull(random);
        System.out.println(random);
    }

    @Test
    void makeNumber() {
        String random = RandomStringUtil.makeNumber(16);
        Assertions.assertNotNull(random);
        System.out.println(random);
    }
}