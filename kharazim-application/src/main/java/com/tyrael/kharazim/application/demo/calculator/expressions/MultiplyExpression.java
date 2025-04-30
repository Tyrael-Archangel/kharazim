package com.tyrael.kharazim.application.demo.calculator.expressions;

import com.tyrael.kharazim.application.demo.calculator.SequenceGroup;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2022/2/9
 */
public class MultiplyExpression extends CalculableExpression {

    private final CalculableExpression first;

    private final CalculableExpression second;

    public MultiplyExpression(CalculableExpression first, CalculableExpression second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public BigDecimal calculate() {
        return first.calculate()
                .multiply(second.calculate());
    }

    /**
     * @author Tyrael Archangel
     * @since 2022/2/9
     */
    public static class MultiplySymbol extends SymbolExpression<MultiplyExpression> {

        @Override
        public SequenceGroup sequence() {
            return SequenceGroup.MULTIPLY_DIVIDE;
        }

        @Override
        public String sign() {
            return "*";
        }

        @Override
        public MultiplyExpression reduce() {

            MultiplyExpression multiplyExpression = new MultiplyExpression(
                    (CalculableExpression) this.prev, (CalculableExpression) this.next);
            connect(multiplyExpression, this.next.next);
            connect(this.prev.prev, multiplyExpression);

            return multiplyExpression;
        }
    }
}
