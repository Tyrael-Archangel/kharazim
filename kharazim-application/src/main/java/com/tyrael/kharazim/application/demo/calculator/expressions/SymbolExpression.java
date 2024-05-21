package com.tyrael.kharazim.application.demo.calculator.expressions;

import com.tyrael.kharazim.application.demo.calculator.SequenceGroup;

/**
 * @author Tyrael Archangel
 * @since 2022/2/9
 */
public abstract class SymbolExpression<T extends CalculableExpression> extends Expression implements Cloneable {

    public abstract SequenceGroup sequence();

    public abstract String sign();

    public abstract T reduce();

    /**
     * 处理简写
     */
    public void processAbbr() {

    }

    @Override
    public String toString() {
        return sign();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
