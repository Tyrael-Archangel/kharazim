package com.tyrael.kharazim.application.demo.calculator.expressions;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2022/2/9
 */
public class NumberExpression extends CalculableExpression {

    private final BigDecimal value;

    public NumberExpression(BigDecimal value) {
        this.value = value;
    }

    public NumberExpression(String value) {
        this(new BigDecimal(value));
    }

    @Override
    public BigDecimal calculate() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
