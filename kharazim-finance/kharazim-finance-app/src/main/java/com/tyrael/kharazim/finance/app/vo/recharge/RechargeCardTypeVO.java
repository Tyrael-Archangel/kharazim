package com.tyrael.kharazim.finance.app.vo.recharge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2024/1/27
 */
@Data
public class RechargeCardTypeVO {

    @Schema(hidden = true)
    private Long id;

    @Schema(description = "储值卡项编码")
    private String code;

    @Schema(description = "储值卡项名称", example = "5000卡")
    private String name;

    @Schema(description = "折扣百分比")
    private BigDecimal discountPercent;

    @Schema(description = "是否永不过期")
    private Boolean neverExpire;

    @Schema(description = "有效期天数")
    private Integer validPeriodDays;

    @Schema(description = "默认卡金额")
    private BigDecimal defaultAmount;

    @Schema(description = "是否可以创建新卡")
    private Boolean canCreateNewCard;

}
