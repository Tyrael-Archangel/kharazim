package com.tyrael.kharazim.application.skupublish.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/3/26
 */
@Data
public class PublishSkuRequest {

    @Schema(description = "sku编码")
    @NotBlank(message = "请指定商品")
    private String skuCode;

    @Schema(description = "诊所（机构）编码")
    @NotBlank(message = "请指定诊所")
    private String clinicCode;

    @Schema(description = "单价")
    @NotNull(message = "请指定单价")
    private BigDecimal price;

    @Schema(description = "生效时间")
    @NotNull(message = "请指定生效时间")
    private LocalDateTime effectBegin;

    @Schema(description = "失效时间")
    @NotNull(message = "请指定失效时间")
    private LocalDateTime effectEnd;

}
