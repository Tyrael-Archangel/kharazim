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
        registerSymbolExpression(
                new AddExpression.AddSymbol(),
                new SubtractExpression.SubtractSymbol(),
                new MultiplyExpression.MultiplySymbol(),
                new DivideExpression.DivideSymbol(),
                new FactorialExpression.FactorialSymbol(),
                new PowerExpression.PowerSymbol(),
                new SinExpression.SinSymbol(),
                new CosExpression.CosSymbol(),
                new BracketSymbolExpression.Left(),
                new BracketSymbolExpression.Right(),
                new PercentExpression.PercentSymbol()
        );
    }

    public void registerSymbolExpression(SymbolExpression<?>... symbolExpressions) {
        for (SymbolExpression<?> symbolExpression : symbolExpressions) {
            symbolExpressionMap.put(symbolExpression.sign(), symbolExpression);
        }
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
