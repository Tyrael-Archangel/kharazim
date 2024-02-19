package com.tyrael.kharazim.application.recharge.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2024/2/19
 */
@Data
@Builder
public class CustomerRechargeCardTypeBalanceVO {

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "会员名称")
    private String customerName;

    @Schema(description = "剩余金额")
    private BigDecimal balanceAmount;

    @Schema(description = "储值卡项编码")
    private String cardTypeCode;

    @Schema(description = "储值卡项名称")
    private String cardTypeName;

}