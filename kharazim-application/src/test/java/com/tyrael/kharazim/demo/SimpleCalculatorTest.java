package com.tyrael.kharazim.demo;

import com.tyrael.kharazim.application.demo.calculator.SimpleCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/4/30
 */
public class SimpleCalculatorTest {

    @Test
    public void calculate() {
        SimpleCalculator calculator = new SimpleCalculator();
        List<String> expressions = List.of(
                "3!+3.3*12+sin45",
                "2*(3*2+4)/3-1",
                "10/(3^2+1)",
                "((10+10)/(3^2+1))",
                "(10+(10)/3^(2+1))");
        for (String expression : expressions) {
            BigDecimal result = calculator.calculate(expression);
            System.out.println(expression + " = " + result);
        }
    }

}
