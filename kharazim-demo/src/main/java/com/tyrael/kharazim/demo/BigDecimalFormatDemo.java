package com.tyrael.kharazim.demo;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2024/3/20
 */
public class BigDecimalFormatDemo {

    public static void main(String[] args) {
        BigDecimal value = new BigDecimal("100.0");
        System.out.println(value);
        System.out.println(value.stripTrailingZeros());
        System.out.println(value.stripTrailingZeros().toPlainString());
    }

}
