package com.tyrael.kharazim.application.demo.calculator.expressions;

import com.tyrael.kharazim.application.demo.calculator.SequenceGroup;

import java.util.Stack;

/**
 * @author Tyrael Archangel
 * @since 2022/2/16
 */
public abstract class BracketSymbolExpression extends SymbolExpression<CalculableExpression> {

    /**
     * 查找与左括号对应的右括号
     */
    public static Right findRightPairBracket(Left left) {

        Stack<BracketSymbolExpression> pairs = new Stack<>();

        Expression node = left;
        while (node != null) {
            if (node instanceof Left leftNode) {
                pairs.push(leftNode);
            } else if (node instanceof Right rightNode) {
                pairs.pop();
                if (pairs.isEmpty()) {
                    return rightNode;
                }
            }

            node = node.next();
        }

        throw new UnsupportedOperationException("括号不成对");
    }

    @Override
    public SequenceGroup sequence() {
        return SequenceGroup.BRACKET;
    }

    @Override
    public CalculableExpression reduce() {
        throw new UnsupportedOperationException();
    }

    public static class Left extends BracketSymbolExpression {

        @Override
        public String sign() {
            return "(";
        }

        @Override
        public void processAbbr() {
            // 如果左边括号前面是数字，则在数字和左边括号之间添加一个乘号
            // 8(3+3) -> 8*(3+3)
            if (this.prev instanceof NumberExpression) {
                MultiplyExpression.MultiplySymbol multiplySymbol = new MultiplyExpression.MultiplySymbol();
                Expression.connect(this.prev, multiplySymbol);
                Expression.connect(multiplySymbol, this);
            }
        }

    }

    public static class Right extends BracketSymbolExpression {

        @Override
        public String sign() {
            return ")";
        }

    }

}
