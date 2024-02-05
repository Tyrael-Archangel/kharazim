package com.tyrael.kharazim.application.recharge.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 储值单退卡申请参数
 *
 * @author Tyrael Archangel
 * @since 2024/2/5
 */
@Data
public class CustomerRechargeCardChargebackRequest {

    @Schema(description = "储值单号")
    @NotBlank(message = "储值单号不能为空")
    private String rechargeCardCode;

    @Schema(description = "退卡金额")
    @NotNull(message = "退卡金额不能为空")
    private BigDecimal chargebackAmount;

    @Schema(description = "退卡员工编码")
    @NotBlank(message = "请选择退卡员工")
    private String chargebackUserCode;

}
