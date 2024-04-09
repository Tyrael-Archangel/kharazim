package com.tyrael.kharazim.demo;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Tyrael Archangel
 * @since 2024/4/9
 */
public class PrintNumberCountDemo {

    public static void main(String[] args) {

        Random random = new Random();
        List<Number> numbers = new ArrayList<>();
        for (int i = 0; i < 400; i++) {
            numbers.add((int) random.nextExponential());
        }

        int from = 0;
        int end = 10;
        double distance = ((double) end - (double) from) / 10;
        print(numbers, from, end, distance);
    }

    public static void print(List<? extends Number> numbers, Number from, Number end, Number distance) {
        BigDecimal fromValue = new BigDecimal(from.toString());
        BigDecimal endValue = new BigDecimal(end.toString());
        if (fromValue.compareTo(endValue) >= 0) {
            throw new IllegalArgumentException("from value must smaller than end value");
        }

        Map<BigDecimal, Integer> valueCountMap = new LinkedHashMap<>();
        BigDecimal distanceValue = new BigDecimal(distance.toString());
        for (BigDecimal i = fromValue.subtract(distanceValue);
             i.compareTo(endValue.add(distanceValue)) <= 0;
             i = i.add(distanceValue)) {
            valueCountMap.put(i, 0);
        }

        List<BigDecimal> values = new ArrayList<>(valueCountMap.keySet());
        for (Number number : numbers) {
            BigDecimal numberValue = new BigDecimal(number.toString());
            BigDecimal closestValue = findClosestValue(values, numberValue);
            valueCountMap.computeIfPresent(closestValue, (k, o) -> o + 1);
        }

        int maxLength = 0;
        for (BigDecimal key : valueCountMap.keySet()) {
            maxLength = Math.max(maxLength, key.toPlainString().length());
        }
        int finalMaxLength = maxLength;
        valueCountMap.forEach((k, v) -> System.out.println(formatNumber(k, finalMaxLength) + "\t: " + "-".repeat(v)));
    }

    private static BigDecimal findClosestValue(List<BigDecimal> values, BigDecimal number) {
        BigDecimal distance = BigDecimal.valueOf(Long.MAX_VALUE);
        BigDecimal result = null;
        for (BigDecimal value : values) {
            BigDecimal currentDistance = value.subtract(number).abs();
            if (currentDistance.compareTo(distance) < 0) {
                result = value;
                distance = currentDistance;
            } else {
                return result;
            }
        }
        return result;
    }

    private static String formatNumber(Number number, int bit) {
        String numberString = number.toString();
        int length = numberString.length();
        if (length > bit) {
            throw new IllegalArgumentException();
        }
        int diffLen = bit - length;
        int frontLen = diffLen / 2;
        int endLen = diffLen - frontLen;
        return " ".repeat(frontLen) + numberString + " ".repeat(endLen);
    }

}
