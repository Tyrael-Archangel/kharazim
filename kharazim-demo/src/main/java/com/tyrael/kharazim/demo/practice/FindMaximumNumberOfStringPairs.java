package com.tyrael.kharazim.demo.practice;

/**
 * 最大字符串配对数目
 * <pre>
 * 给你一个下标从 0 开始的数组 words ，数组中包含 互不相同 的字符串。
 * 如果字符串 words[i] 与字符串 words[j] 满足以下条件，我们称它们可以匹配：
 * 字符串 words[i] 等于 words[j] 的反转字符串。
 * 0 <= i < j < words.length
 * 请你返回数组 words 中的 最大 匹配数目。
 *
 * 注意，每个字符串最多匹配一次。
 * </pre>
 *
 * @author Tyrael Archangel
 * @since 2024/1/17
 */
public class FindMaximumNumberOfStringPairs {

    public static void main(String[] args) {
        String[] words = {"cd", "ac", "dc", "ca", "zz"};
        int maximumNumber = maximumNumberOfStringPairs(words);
        System.out.println(maximumNumber);
    }

    private static int maximumNumberOfStringPairs(String[] words) {
        int result = 0;
        for (int i = 0; i < words.length; i++) {
            for (int j = i + 1; j < words.length; j++) {
                if (isPair(words[i], words[j])) {
                    result++;
                }
            }
        }
        return result;
    }

    private static boolean isPair(String s1, String s2) {
        int length = s1.length();
        if (length != s2.length()) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (s1.charAt(i) != s2.charAt(length - i - 1)) {
                return false;
            }
        }
        return true;
    }

}
