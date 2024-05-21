package com.tyrael.kharazim.application.demo.calculator.expressions;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2022/2/9
 */
public abstract class CalculableExpression extends Expression {

    public abstract BigDecimal calculate();

}
