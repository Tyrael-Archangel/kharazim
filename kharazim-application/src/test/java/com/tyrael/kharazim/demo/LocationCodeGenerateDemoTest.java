package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

/**
 * @author Tyrael Archangel
 * @since 2024/10/22
 */
public class LocationCodeGenerateDemoTest {

    @Test
    public void locationCodeGenerate() {
        String begin = "A1-B1-100";
        String end = "B1-C1-100";

        check(begin, end);

        Stream<String> generator = Stream.iterate(begin, x -> x.compareTo(end) <= 0, this::increase);
        generator.forEach(System.out::println);

    }

    private void check(String begin, String end) {
        if (begin.length() != end.length()) {
            throw new IllegalArgumentException("长度不一样");
        }

        for (int i = 0; i < begin.length(); i++) {
            if (!sameKind(begin.charAt(i), end.charAt(i))) {
                throw new IllegalArgumentException("格式不合法");
            }
        }

        if (begin.compareTo(end) > 0) {
            throw new IllegalArgumentException("格式不合法");
        }
    }

    private boolean sameKind(char a, char b) {
        if (Character.isDigit(a)) {
            return Character.isDigit(b);
        }
        if (Character.isUpperCase(a)) {
            return Character.isUpperCase(b);
        }
        if (Character.isLowerCase(a)) {
            return Character.isLowerCase(b);
        }
        return a == b;
    }

    private String increase(String s) {
        char[] chars = s.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            char c = chars[i];

            if (Character.isDigit(c) || Character.isLetter(c)) {
                if (c == '9') {
                    chars[i] = '0';
                } else if (c == 'z') {
                    chars[i] = 'a';
                } else if (c == 'Z') {
                    chars[i] = 'A';
                } else {
                    chars[i] = ++c;
                    return new String(chars);
                }
            }
        }
        return new String(chars);
    }

}
