package com.tyrael.kharazim.demo.chat.house;

import java.util.Random;

/**
 * @author Tyrael Archangel
 * @since 2023/12/29
 */
public class BytesUtil {

    private static final int BYTE_SIZE = 4;

    public static byte[] toBytes(int i) {
        byte[] bytes = new byte[BYTE_SIZE];
        bytes[0] = (byte) i;
        bytes[1] = (byte) (i >> 8);
        bytes[2] = (byte) (i >> 16);
        bytes[3] = (byte) (i >> 24);
        return bytes;
    }

    public static int toInt(byte[] bytes) {
        if (bytes == null || bytes.length != BYTE_SIZE) {
            throw new IllegalArgumentException();
        }
        int i = bytes[0] & 0xff;
        i |= (bytes[1] & 0xff) << 8;
        i |= (bytes[2] & 0xff) << 16;
        i |= (bytes[3] & 0xff) << 24;
        return i;
    }

    public static byte[] mergeBytes(byte[] bytes1, byte[] bytes2) {
        byte[] merge = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, merge, 0, bytes1.length);
        System.arraycopy(bytes2, 0, merge, bytes1.length, bytes2.length);
        return merge;
    }


    public static void main(String[] args) {
        int i = new Random().nextInt((1000000));
        byte[] bytes = toBytes(i);
        int x = toInt(bytes);
        System.out.println(i);
        System.out.println(x);
        System.out.println(0xff);

    }

}
