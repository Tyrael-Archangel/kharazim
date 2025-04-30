package com.tyrael.kharazim.application.demo.calculator.expressions;

import com.tyrael.kharazim.application.demo.calculator.SequenceGroup;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2022/2/17
 */
public class CosExpression extends CalculableExpression {

    private final CalculableExpression angle;

    public CosExpression(CalculableExpression angle) {
        this.angle = angle;
    }

    @Override
    public BigDecimal calculate() {
        return BigDecimal.valueOf(Math.cos(angle.calculate().doubleValue()));
    }

    /**
     * @author Tyrael Archangel
     * @since 2022/2/17
     */
    public static class CosSymbol extends SymbolExpression<CosExpression> {

        @Override
        public SequenceGroup sequence() {
            return SequenceGroup.TRIGONOMETRIC;
        }

        @Override
        public String sign() {
            return "cos";
        }

        @Override
        public CosExpression reduce() {

            CosExpression cos = new CosExpression((CalculableExpression) this.next);
            connect(cos, this.next.next);
            connect(this.prev, cos);

            return cos;

        }

    }
}
