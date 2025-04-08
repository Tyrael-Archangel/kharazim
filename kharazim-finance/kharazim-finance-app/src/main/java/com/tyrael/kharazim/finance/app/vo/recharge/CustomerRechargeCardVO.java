package com.tyrael.kharazim.finance.app.vo.recharge;

import com.tyrael.kharazim.finance.app.enums.CustomerRechargeCardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Tyrael Archangel
 * @since 2024/2/4
 */
@Data
@Builder
public class CustomerRechargeCardVO {

    @Schema(description = "储值单号")
    private String code;

    @Schema(description = "状态，" + CustomerRechargeCardStatus.DESC)
    private CustomerRechargeCardStatus status;

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "会员姓名")
    private String customerName;

    @Schema(description = "储值卡项编码")
    private String cardTypeCode;

    @Schema(description = "储值卡项名称")
    private String cardTypeName;

    @Schema(description = "储值金额")
    private BigDecimal amount;

    @Schema(description = "剩余储值金额")
    private BigDecimal balanceAmount;

    @Schema(description = "已消耗金额")
    private BigDecimal consumedAmount;

    @Schema(description = "已消耗商品原价金额")
    private BigDecimal consumedOriginalAmount;

    @Schema(description = "成交员工编码")
    private String traderUserCode;

    @Schema(description = "成交员工姓名")
    private String traderUserName;

    @Schema(description = "折扣百分比")
    private BigDecimal discountPercent;

    @Schema(description = "是否永不过期")
    private Boolean neverExpire;

    @Schema(description = "有效期限，永不过期时为null")
    private LocalDate expireDate;

    @Schema(description = "储值日期")
    private LocalDate rechargeDate;

    @Schema(description = "退卡金额")
    private BigDecimal chargebackAmount;

    @Schema(description = "退卡员工编码")
    private String chargebackUserCode;

    @Schema(description = "退卡员工")
    private String chargebackUserName;

}
