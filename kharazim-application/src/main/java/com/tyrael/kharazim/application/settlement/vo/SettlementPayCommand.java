package com.tyrael.kharazim.application.settlement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Data
public class SettlementPayCommand {

    @Schema(description = "结算单编码")
    @NotBlank(message = "请指定结算单")
    private String settlementOrderCode;

    @Schema(description = "使用储值单结算详情")
    @NotEmpty(message = "请指定结算详情")
    private List<RechargeCardPayDetail> rechargeCardPayDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RechargeCardPayDetail {

        @Schema(description = "储值单号")
        @NotBlank(message = "请指定储值单号")
        private String rechargeCardCode;

        @Schema(description = "使用金额")
        @NotBlank(message = "请指定使用金额")
        @Positive(message = "使用金额必须大于0")
        private BigDecimal useAmount;

        @Schema(description = "抵扣结算单金额")
        @NotBlank(message = "请指定抵扣金额")
        @Positive(message = "抵扣金额必须大于0")
        private BigDecimal deductAmount;

    }

}
