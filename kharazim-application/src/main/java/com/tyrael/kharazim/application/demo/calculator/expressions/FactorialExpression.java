package com.tyrael.kharazim.application.demo.calculator.expressions;

import com.tyrael.kharazim.application.demo.calculator.SequenceGroup;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Tyrael Archangel
 * @since 2022/2/9
 */
public class FactorialExpression extends CalculableExpression {

    private final BigDecimal max = new BigDecimal(60);

    private final CalculableExpression bottom;

    public FactorialExpression(CalculableExpression bottom) {
        this.bottom = bottom;
    }

    @Override
    public BigDecimal calculate() {
        BigDecimal calculate = bottom.calculate();

        if (!isNonNegativeInteger(calculate)) {
            // 非负整数
            throw new UnsupportedOperationException("invalid factorial param: " + calculate);
        }

        if (calculate.compareTo(max) > 0) {
            throw new UnsupportedOperationException("invalid factorial param, too big");
        }

        BigDecimal result = BigDecimal.ONE;
        for (BigDecimal i = BigDecimal.ONE; i.compareTo(calculate) <= 0; i = i.add(BigDecimal.ONE)) {
            result = result.multiply(i);
        }

        return result;

    }

    /**
     * 是否 非负整数
     */
    private boolean isNonNegativeInteger(BigDecimal number) {

        try {
            return number.toBigIntegerExact()
                    .compareTo(BigInteger.ZERO) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @author Tyrael Archangel
     * @since 2022/2/9
     */
    public static class FactorialSymbol extends SymbolExpression<FactorialExpression> {

        @Override
        public SequenceGroup sequence() {
            return SequenceGroup.TRIGONOMETRIC;
        }

        @Override
        public String sign() {
            return "!";
        }

        @Override
        public FactorialExpression reduce() {

            FactorialExpression factorialExpression = new FactorialExpression((CalculableExpression) this.prev);
            join(factorialExpression, this.next);
            join(this.prev.prev, factorialExpression);

            return factorialExpression;

        }

    }
}
