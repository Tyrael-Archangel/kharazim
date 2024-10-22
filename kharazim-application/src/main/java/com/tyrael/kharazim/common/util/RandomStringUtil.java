package com.tyrael.kharazim.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomStringUtil {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private static final char[] SYMBOL;
    private static final char[] DIGIT_SYMBOL;

    static {
        StringBuilder symbol = new StringBuilder();
        for (char character = '0'; character <= '9'; character++) {
            symbol.append(character);
        }
        DIGIT_SYMBOL = symbol.toString().toCharArray();
        for (char character = 'a'; character <= 'z'; character++) {
            symbol.append(character);
        }
        for (char character = 'A'; character <= 'Z'; character++) {
            symbol.append(character);
        }
        SYMBOL = symbol.toString().toCharArray();
    }

    public static String make() {
        return make(8);
    }

    public static String make(int length) {
        char[] buffer = new char[length];
        for (int index = 0; index < length; index++) {
            buffer[index] = SYMBOL[RANDOM.nextInt(SYMBOL.length)];
        }
        return new String(buffer);
    }

    public static String makeNumber(int length) {
        char[] buffer = new char[length];
        for (int index = 0; index < length; index++) {
            buffer[index] = DIGIT_SYMBOL[RANDOM.nextInt(DIGIT_SYMBOL.length)];
        }
        return new String(buffer);
    }

}
