package com.tyrael.kharazim.finance.app.vo.recharge;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Tyrael Archangel
 * @since 2024/2/1
 */
@Data
public class CustomerRechargeRequest {

    @Schema(description = "会员编码")
    @NotEmpty(message = "会员编码不能为空")
    private String customerCode;

    @Schema(description = "储值卡项编码")
    @NotEmpty(message = "请指定储值卡项")
    private String cardTypeCode;

    @Schema(description = "储值日期", example = "2023-10-07")
    @NotNull(message = "储值日期不能为空")
    private LocalDate rechargeDate;

    @Schema(description = "充值金额")
    @NotNull(message = "充值金额不能为空")
    @Min(value = 1, message = "充值金额不能小于1元")
    @Max(value = 1_000_000, message = "充值金额不能大于1000000元")
    private BigDecimal amount;

    @Schema(description = "成交员工编码")
    @NotEmpty(message = "请指定成交员工")
    private String traderUserCode;

    @Schema(description = "备注信息", maxLength = 255)
    @Size(max = 255, message = "备注信息超长")
    private String remark;

}
