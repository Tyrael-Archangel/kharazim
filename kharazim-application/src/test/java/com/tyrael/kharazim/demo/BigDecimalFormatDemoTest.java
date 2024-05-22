package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2024/3/20
 */
public class BigDecimalFormatDemoTest {

    @Test
    public void bigDecimalFormat() {
        BigDecimal value = new BigDecimal("100.0");
        System.out.println(value);
        System.out.println(value.stripTrailingZeros());
        System.out.println(value.stripTrailingZeros().toPlainString());
    }

}
