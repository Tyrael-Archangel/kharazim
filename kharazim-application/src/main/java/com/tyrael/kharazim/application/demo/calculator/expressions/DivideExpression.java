package com.tyrael.kharazim.application.demo.calculator.expressions;

import com.tyrael.kharazim.application.demo.calculator.SequenceGroup;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Tyrael Archangel
 * @since 2022/2/9
 */
public class DivideExpression extends CalculableExpression {

    private final CalculableExpression first;

    private final CalculableExpression second;

    public DivideExpression(CalculableExpression first, CalculableExpression second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public BigDecimal calculate() {
        return first.calculate()
                .divide(second.calculate(), 30, RoundingMode.HALF_UP)
                .stripTrailingZeros();
    }

    /**
     * @author Tyrael Archangel
     * @since 2022/2/9
     */
    public static class DivideSymbol extends SymbolExpression<DivideExpression> {

        @Override
        public SequenceGroup sequence() {
            return SequenceGroup.MULTIPLY_DIVIDE;
        }

        @Override
        public String sign() {
            return "/";
        }

        @Override
        public DivideExpression reduce() {

            DivideExpression divideExpression = new DivideExpression(
                    (CalculableExpression) this.prev, (CalculableExpression) this.next);
            join(divideExpression, this.next.next);
            join(this.prev.prev, divideExpression);

            return divideExpression;
        }

    }
}
