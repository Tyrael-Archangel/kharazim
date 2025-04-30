package com.tyrael.kharazim.application.demo.calculator.expressions;

import com.tyrael.kharazim.application.demo.calculator.SequenceGroup;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2022/2/9
 */
public class SinExpression extends CalculableExpression {

    private final CalculableExpression angle;

    public SinExpression(CalculableExpression angle) {
        this.angle = angle;
    }

    @Override
    public BigDecimal calculate() {
        double sin = Math.sin(angle.calculate().doubleValue());
        return BigDecimal.valueOf(sin);
    }

    /**
     * @author Tyrael Archangel
     * @since 2022/2/9
     */
    public static class SinSymbol extends SymbolExpression<SinExpression> {

        @Override
        public SequenceGroup sequence() {
            return SequenceGroup.TRIGONOMETRIC;
        }

        @Override
        public String sign() {
            return "sin";
        }

        @Override
        public SinExpression reduce() {

            SinExpression sinExpression = new SinExpression((CalculableExpression) this.next);
            connect(sinExpression, this.next.next);
            connect(this.prev, sinExpression);

            return sinExpression;

        }

    }
}
