package com.tyrael.kharazim.application.demo.calculator.expressions;

import com.tyrael.kharazim.application.demo.calculator.SequenceGroup;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2022/2/17
 */
public class PowerExpression extends CalculableExpression {

    private final CalculableExpression base;
    private final CalculableExpression exponent;

    public PowerExpression(CalculableExpression base, CalculableExpression exponent) {
        this.base = base;
        this.exponent = exponent;
    }

    @Override
    public BigDecimal calculate() {
        BigDecimal baseValue = base.calculate();
        BigDecimal exponentValue = exponent.calculate();

        double pow = Math.pow(baseValue.doubleValue(), exponentValue.doubleValue());
        return new BigDecimal(pow);
    }

    /**
     * @author Tyrael Archangel
     * @since 2022/2/17
     */
    public static class PowerSymbol extends SymbolExpression<PowerExpression> {

        @Override
        public SequenceGroup sequence() {
            return SequenceGroup.TRIGONOMETRIC;
        }

        @Override
        public String sign() {
            return "^";
        }

        @Override
        public PowerExpression reduce() {

            PowerExpression powerExpression = new PowerExpression(
                    (CalculableExpression) this.prev, (CalculableExpression) this.next);

            connect(powerExpression, this.next.next);
            connect(this.prev.prev, powerExpression);

            return powerExpression;

        }

    }
}
