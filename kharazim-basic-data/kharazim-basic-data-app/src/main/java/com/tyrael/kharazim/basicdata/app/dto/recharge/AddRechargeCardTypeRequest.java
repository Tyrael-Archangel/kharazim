package com.tyrael.kharazim.basicdata.app.dto.recharge;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Tyrael Archangel
 * @since 2024/1/25
 */
@Data
public class AddRechargeCardTypeRequest {

    @Schema(description = "储值卡项名称")
    @NotBlank(message = "储值卡项名称不能为空")
    @Size(max = 64, message = "储值卡项名称超长")
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

    public static AddRechargeCardTypeRequest create(String name,
                                                    int discountPercent,
                                                    boolean neverExpire,
                                                    Integer validPeriodDays,
                                                    int defaultAmount) {
        AddRechargeCardTypeRequest request = new AddRechargeCardTypeRequest();
        request.name = name;
        request.discountPercent = BigDecimal.valueOf(discountPercent);
        request.neverExpire = neverExpire;
        request.validPeriodDays = neverExpire ? null : Objects.requireNonNull(validPeriodDays);
        request.defaultAmount = BigDecimal.valueOf(defaultAmount);
        return request;
    }

}
