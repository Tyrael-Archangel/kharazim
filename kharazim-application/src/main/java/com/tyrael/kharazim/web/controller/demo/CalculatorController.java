package com.tyrael.kharazim.web.controller.demo;

import com.tyrael.kharazim.application.demo.calculator.CalculatorComponent;
import com.tyrael.kharazim.common.dto.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2024/5/21
 */
@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
@Tag(name = "计算器")
public class CalculatorController {

    private final CalculatorComponent calculatorComponent;

    @PostMapping("/calculate")
    @Operation(summary = "计算表达式")
    public DataResponse<ExpressionResult> calculate(@RequestBody ExpressionBody expressionBody) {
        String expression = expressionBody.getExpression();
        BigDecimal result = calculatorComponent.calculate(expression);
        return DataResponse.ok(new ExpressionResult(expression, result));
    }

    @Data
    public static class ExpressionBody {

        @Schema(description = "计算表达式")
        private String expression;

    }

    @Data
    @AllArgsConstructor
    public static class ExpressionResult {

        @Schema(description = "计算表达式")
        private String expression;

        @Schema(description = "计算结果")
        private BigDecimal result;

    }

}
