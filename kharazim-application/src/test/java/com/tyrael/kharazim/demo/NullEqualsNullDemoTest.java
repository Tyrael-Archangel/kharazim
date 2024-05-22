package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
@SuppressWarnings("all")
public class NullEqualsNullDemoTest {

    @Test
    public void nullEqualsNull() {
        Object a = null;
        Object b = null;
        System.out.println(a == b);
    }

}
