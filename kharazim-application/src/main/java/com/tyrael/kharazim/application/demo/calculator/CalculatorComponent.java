package com.tyrael.kharazim.application.demo.calculator;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2024/5/21
 */
@Component
public class CalculatorComponent {

    private volatile SimpleCalculator simpleCalculator;

    /**
     * 计算
     *
     * @param expression 表达式
     * @return 结果
     */
    public BigDecimal calculate(String expression) {
        return getCalculator().calculate(expression);
    }

    private SimpleCalculator getCalculator() {
        if (simpleCalculator == null) {
            synchronized (this) {
                if (simpleCalculator == null) {
                    simpleCalculator = new SimpleCalculator();
                }
            }
        }
        return simpleCalculator;
    }

}
