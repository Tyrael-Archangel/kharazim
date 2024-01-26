package com.tyrael.kharazim.application.recharge.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2024/1/26
 */
@Data
public class ModifyRechargeCardTypeRequest {

    @Schema(description = "储值卡项编码")
    @NotBlank(message = "储值卡项编码不能为空")
    private String code;

    @Schema(description = "储值卡项名称")
    @NotBlank(message = "储值卡项名称不能为空")
    private String name;

    @Schema(description = "折扣百分比")
    @NotNull(message = "折扣百分比不能为空")
    private BigDecimal discountPercent;

    @Schema(description = "是否永不过期")
    @NotNull(message = "请设置过期失效")
    private Boolean neverExpire;

    @Schema(description = "有效期天数，永不过期为false时，必传")
    private Integer validPeriodDays;

    @Schema(description = "默认卡金额")
    @NotNull(message = "默认卡金额不能为空")
    private BigDecimal defaultAmount;

}
