package com.tyrael.kharazim.application.demo.calculator.expressions;

import com.tyrael.kharazim.application.demo.calculator.SequenceGroup;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2022/2/9
 */
public class SubtractExpression extends CalculableExpression {

    private final CalculableExpression first;

    private final CalculableExpression second;

    public SubtractExpression(CalculableExpression first, CalculableExpression second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public BigDecimal calculate() {
        return first.calculate()
                .subtract(second.calculate());
    }

    /**
     * @author Tyrael Archangel
     * @since 2022/2/9
     */
    public static class SubtractSymbol extends SymbolExpression<SubtractExpression> {

        @Override
        public SequenceGroup sequence() {
            return SequenceGroup.ADD_SUBTRACT;
        }

        @Override
        public String sign() {
            return "-";
        }

        @Override
        public SubtractExpression reduce() {

            SubtractExpression subtractExpression = new SubtractExpression(
                    (CalculableExpression) this.prev, (CalculableExpression) this.next);
            connect(subtractExpression, this.next.next);
            connect(this.prev.prev, subtractExpression);

            return subtractExpression;
        }

        @Override
        public void processAbbr() {
            // 如果减号左边是数字或者右括号就不管，例如：2-3，(1+2)-3
            if (this.prev instanceof NumberExpression
                    || this.prev instanceof BracketSymbolExpression.Right) {
                return;
            }

            // 如果左边是其它符号，就处理，在减号左边加[左括号]和[0]，在减号右边的表达式末尾加上[右括号]
            // 例如：
            // (-3+4) => ((0-3)+4)
            // 2*-3+4 => 2*(0-3)+4
            // -(3+4)-2 => (0-(3+4))-2
            BracketSymbolExpression.Left leftBracket = new BracketSymbolExpression.Left();
            BracketSymbolExpression.Right rightBracket = new BracketSymbolExpression.Right();
            NumberExpression zero = new NumberExpression(BigDecimal.ZERO);

            Expression nextExpression;
            if (this.next instanceof BracketSymbolExpression.Left nextLeftBracket) {
                nextExpression = BracketSymbolExpression.findRightPairBracket(nextLeftBracket);
            } else {
                nextExpression = this.next;
            }

            Expression.connect(this.prev, leftBracket);
            Expression.connect(leftBracket, zero);
            Expression.connect(zero, this);
            Expression.connect(rightBracket, nextExpression.next);
            Expression.connect(nextExpression, rightBracket);
        }

    }
}
