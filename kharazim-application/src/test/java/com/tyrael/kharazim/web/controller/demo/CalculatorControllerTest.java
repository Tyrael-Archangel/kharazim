package com.tyrael.kharazim.web.controller.demo;

import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/5/21
 */
class CalculatorControllerTest extends BaseControllerTest<CalculatorController> {

    CalculatorControllerTest() {
        super(CalculatorController.class);
    }

    @Test
    void calculate() {

        List<String> expressions = List.of(
                "3!+3.3*12+sin45",
                "2*(3*2+4)/3-1",
                "10/(3^2+1)",
                "((10+10)/(3^2+1))",
                "(10+(10)/3^(2+1))");
        for (String expression : expressions) {
            CalculatorController.ExpressionBody expressionBody = new CalculatorController.ExpressionBody();
            expressionBody.setExpression(expression);
            super.performWhenCall(mockController.calculate(expressionBody));
        }

    }

}
