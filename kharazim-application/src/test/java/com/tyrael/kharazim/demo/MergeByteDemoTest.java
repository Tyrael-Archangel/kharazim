package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author Tyrael Archangel
 * @since 2023/12/29
 */
public class MergeByteDemoTest {

    @Test
    public void mergeByte() {
        byte[] bytes1 = {11, 22, 33};
        byte[] bytes2 = {33, 44, 55, 66, 77, 88};
        byte[] mergeBytes = mergeBytes(bytes1, bytes2);
        System.out.println(Arrays.toString(mergeBytes));
    }

    private byte[] mergeBytes(byte[] bytes1, byte[] bytes2) {
        byte[] merge = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, merge, 0, bytes1.length);
        System.arraycopy(bytes2, 0, merge, bytes1.length, bytes2.length);
        return merge;
    }

}
