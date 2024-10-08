package com.tyrael.kharazim.application.demo.calculator;

import com.tyrael.kharazim.application.demo.calculator.expressions.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2022/2/9
 */
public class SimpleCalculator {

    private final SymbolExpressionFactory symbolExpressionFactory;

    public SimpleCalculator() {
        this(new NormalSymbolExpressionFactory());
    }

    public SimpleCalculator(SymbolExpressionFactory symbolExpressionFactory) {
        this.symbolExpressionFactory = symbolExpressionFactory;
    }

    public BigDecimal calculate(String expression) {

        Expression head = analysis(expression);

        // 处理某些符号的简写
        head = processAbbreviation(head);

        CalculableExpression calculableExpression = shrinkBracket(head);

        return calculableExpression.calculate();

    }

    private Expression analysis(String originalExpression) {
        String expression = originalExpression.replaceAll("\\s+", "");

        Expression.VirtualHeadExpression headPrev = new Expression.VirtualHeadExpression();
        Expression node = headPrev;
        Boolean lastIsNumber = null;
        int from = 0;
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            boolean isNumber = Character.isDigit(c) || c == '.';
            if (lastIsNumber == null) {
                lastIsNumber = isNumber;
                continue;
            }

            String lastExpression = expression.substring(from, i);
            if (!isNumber) {
                if (lastIsNumber) {
                    // 123+22
                    //    ↑
                    node = node.append(new NumberExpression(lastExpression));
                    from = i;
                } else {
                    // 123+sin45
                    //     ↑
                    if (symbolExpressionFactory.effective(lastExpression)) {
                        node = node.append(symbolExpressionFactory.create(lastExpression));
                        from = i;
                    }
                }
            } else {
                if (!lastIsNumber) {
                    // 123+sin45
                    //        ↑
                    node = node.append(symbolExpressionFactory.create(lastExpression));
                    from = i;
                }
            }
            lastIsNumber = isNumber;

        }

        String last = expression.substring(from);
        if (symbolExpressionFactory.effective(last)) {
            node.append(symbolExpressionFactory.create(last));
        } else {
            node.append(new NumberExpression(last));
        }

        return headPrev.pop();
    }

    /**
     * 处理简写，例如：-3*4 处理成 (0-3)*4
     */
    private Expression processAbbreviation(Expression head) {
        Expression.VirtualHeadExpression headPrev = new Expression.VirtualHeadExpression(head);
        Expression node = head;
        while (node != null) {
            if (node instanceof SymbolExpression<?> symbolNode) {
                symbolNode.processAbbr();
            }
            node = node.next();
        }
        return headPrev.pop();
    }

    private CalculableExpression shrinkBracket(Expression head) {

        Expression node = head;
        while (node != null) {

            if (node instanceof BracketSymbolExpression.Left left) {
                Expression bracketExpression = left.next();
                BracketSymbolExpression.Right right = BracketSymbolExpression.findRightPairBracket(left);
                if (left.next() == right) {
                    throw new UnsupportedOperationException("括号对有误");
                }

                // 把bracketExpression独立成一个单独的Expression
                Expression.disconnect(left, bracketExpression);
                Expression.disconnect(right.prev(), right);

                Expression subExpression = shrinkBracket(bracketExpression);
                // 把subExpression链接回去
                Expression.join(left.prev(), subExpression);
                Expression.join(subExpression, right.next());

                if (node == head) {
                    // head为左括号的情况
                    head = subExpression;
                }

                node = subExpression;

            } else if (node instanceof BracketSymbolExpression.Right) {
                throw new UnsupportedOperationException("括号不成对");
            }

            node = node.next();

        }

        return reduce(head);
    }

    public CalculableExpression reduce(Expression head) {

        List<SequenceGroup> sequenceGroups = SequenceGroup.getSequences();
        for (SequenceGroup sequenceGroup : sequenceGroups) {

            head = reduce(head, sequenceGroup);

        }

        return (CalculableExpression) head;
    }

    private Expression reduce(Expression head, SequenceGroup sequenceGroup) {

        Expression.VirtualHeadExpression headPrev = new Expression.VirtualHeadExpression(head);

        Expression node = head;
        while (node != null) {

            if (node instanceof SymbolExpression<?> symbolExpression) {
                if (sequenceGroup.equals(symbolExpression.sequence())) {
                    node = symbolExpression.reduce();
                }
            }

            node = node.next();
        }

        return headPrev.pop();
    }

}
