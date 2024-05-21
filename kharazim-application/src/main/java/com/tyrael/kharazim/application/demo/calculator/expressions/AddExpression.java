package com.tyrael.kharazim.application.demo.calculator.expressions;


import com.tyrael.kharazim.application.demo.calculator.SequenceGroup;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2022/2/9
 */
public class AddExpression extends CalculableExpression {

    private final CalculableExpression first;

    private final CalculableExpression second;

    public AddExpression(CalculableExpression first, CalculableExpression second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public BigDecimal calculate() {
        return first.calculate()
                .add(second.calculate());
    }

    public static class AddSymbol extends SymbolExpression<AddExpression> {

        @Override
        public SequenceGroup sequence() {
            return SequenceGroup.ADD_SUBTRACT;
        }

        @Override
        public String sign() {
            return "+";
        }

        @Override
        public AddExpression reduce() {

            AddExpression addExpression = new AddExpression(
                    (CalculableExpression) this.prev, (CalculableExpression) this.next);
            join(addExpression, this.next.next);
            join(this.prev.prev, addExpression);

            return addExpression;
        }
    }
}
