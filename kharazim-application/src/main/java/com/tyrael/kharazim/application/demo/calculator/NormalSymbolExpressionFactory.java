package com.tyrael.kharazim.application.demo.calculator;

import com.tyrael.kharazim.application.demo.calculator.expressions.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyrael Archangel
 * @since 2022/2/10
 */
public class NormalSymbolExpressionFactory implements SymbolExpressionFactory {

    private final Map<String, SymbolExpression<?>> symbolExpressionMap = new ConcurrentHashMap<>();

    public NormalSymbolExpressionFactory() {
        initSymbolExpressionInstances();
    }

    private void initSymbolExpressionInstances() {
        registerSymbolExpression(new AddExpression.AddSymbol());
        registerSymbolExpression(new SubtractExpression.SubtractSymbol());
        registerSymbolExpression(new MultiplyExpression.MultiplySymbol());
        registerSymbolExpression(new DivideExpression.DivideSymbol());
        registerSymbolExpression(new FactorialExpression.FactorialSymbol());
        registerSymbolExpression(new PowerExpression.PowerSymbol());
        registerSymbolExpression(new SinExpression.SinSymbol());
        registerSymbolExpression(new CosExpression.CosSymbol());
        registerSymbolExpression(new BracketSymbolExpression.Left());
        registerSymbolExpression(new BracketSymbolExpression.Right());
        registerSymbolExpression(new PercentExpression.PercentSymbol());
    }

    public void registerSymbolExpression(SymbolExpression<?> symbolExpression) {
        symbolExpressionMap.put(symbolExpression.sign(), symbolExpression);
    }

    @Override
    public SymbolExpression<?> create(String symbol) {
        SymbolExpression<?> symbolExpression = symbolExpressionMap.get(symbol);
        if (symbolExpression == null) {
            throw new UnsupportedOperationException("symbol '" + symbol + "' not support");
        }

        try {
            return (SymbolExpression<?>) symbolExpression.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public boolean effective(String symbol) {
        return symbolExpressionMap.containsKey(symbol);
    }

}
