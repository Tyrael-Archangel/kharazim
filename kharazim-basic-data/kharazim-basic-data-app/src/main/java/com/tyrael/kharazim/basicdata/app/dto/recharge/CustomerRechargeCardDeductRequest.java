package com.tyrael.kharazim.basicdata.app.dto.recharge;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2024/5/13
 */
@Data
public class CustomerRechargeCardDeductRequest {

    /**
     * 储值单号
     */
    private String rechargeCardCode;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 使用金额
     */
    private BigDecimal useAmount;

    /**
     * 抵扣金额
     */
    @Schema(description = "抵扣结算单金额")
    @NotBlank(message = "请指定抵扣金额")
    @Positive(message = "抵扣金额必须大于0")
    private BigDecimal deductAmount;

}
