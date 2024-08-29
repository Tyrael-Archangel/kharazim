package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tyrael Archangel
 * @since 2024/8/29
 */
public class StringReplaceDemoTest {

    @Test
    public void testReplace() {
        String template = "My name is ${NAME}, i am ${AGE}";
        Map<String, String> replacements = new HashMap<>();
        replacements.put("NAME", "tyrael");
        replacements.put("AGE", "3000");
        String result = format(template, replacements);
        System.out.println(result);
    }

    private String format(String template, Map<String, String> params) {
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)}");
        Matcher matcher = pattern.matcher(template);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String value = params.getOrDefault(matcher.group(1), "");
            matcher.appendReplacement(result, value);
        }
        matcher.appendTail(result);
        return result.toString();
    }

}
