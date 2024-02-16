package com.tyrael.kharazim.application.recharge.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Tyrael Archangel
 * @since 2024/2/16
 */
@Data
@Builder
public class CustomerBalanceOverviewVO {

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "会员名称")
    private String customerName;

    @Schema(description = "剩余结存金额")
    private BigDecimal totalBalanceAmount;

    @Schema(description = "累计充值金额")
    private BigDecimal accumulatedRechargeAmount;

    @Schema(description = "累计消费金额")
    private BigDecimal accumulatedConsumedAmount;

    @Schema(description = "最近即将过期金额")
    private BigDecimal latestExpireAmount;

    @Schema(description = "即将过期金额的有效期限，null表示没有即将过期的金额", example = "2023-08-30")
    private LocalDate expireDate;

}
