package com.tyrael.kharazim.application.demo.calculator.expressions;

import com.tyrael.kharazim.application.demo.calculator.SequenceGroup;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Tyrael Archangel
 * @since 2024/5/21
 */
public class PercentExpression extends CalculableExpression {

    private final BigDecimal oneHundred = BigDecimal.valueOf(100);
    private final CalculableExpression numerator;

    public PercentExpression(CalculableExpression numerator) {
        this.numerator = numerator;
    }

    @Override
    public BigDecimal calculate() {
        BigDecimal numeratorValue = numerator.calculate();
        return numeratorValue.divide(oneHundred, numeratorValue.scale() + 2, RoundingMode.HALF_UP);
    }

    public static class PercentSymbol extends SymbolExpression<PercentExpression> {

        @Override
        public SequenceGroup sequence() {
            return SequenceGroup.PERCENT;
        }

        @Override
        public String sign() {
            return "%";
        }

        @Override
        public PercentExpression reduce() {

            PercentExpression percentExpression = new PercentExpression((CalculableExpression) this.prev);
            connect(percentExpression, this.next);
            connect(this.prev.prev, percentExpression);

            return percentExpression;
        }
    }

}
