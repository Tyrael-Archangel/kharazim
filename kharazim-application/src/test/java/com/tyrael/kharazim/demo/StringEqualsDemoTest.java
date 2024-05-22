package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/2/19
 */
public class StringEqualsDemoTest {

    @Test
    @SuppressWarnings("all")
    public void stringEquals() {

        String s1 = "Hello";
        System.out.println(s1 == "Hello"); // true

        String s2 = new String("World");
        System.out.println(s2 == "World"); // false

        String s3 = new String("HelloWorld").intern();
        System.out.println(s3 == "HelloWorld"); // true

    }

}
