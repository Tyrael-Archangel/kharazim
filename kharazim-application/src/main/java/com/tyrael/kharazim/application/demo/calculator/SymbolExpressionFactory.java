package com.tyrael.kharazim.application.demo.calculator;

import com.tyrael.kharazim.application.demo.calculator.expressions.SymbolExpression;

/**
 * @author Tyrael Archangel
 * @since 2022/2/10
 */
public interface SymbolExpressionFactory {

    SymbolExpression<?> create(String symbol);

    boolean effective(String symbol);

}
