package com.tyrael.kharazim.purchase.app.vo.purchaseorder;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/5/31
 */
@Data
public class CreatePurchaseOrderRequest {

    @Schema(description = "诊所编码")
    @NotBlank(message = "请指定诊所")
    private String clinicCode;

    @Schema(description = "供应商编码")
    @NotBlank(message = "请指定供应商")
    private String supplierCode;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "采购商品")
    @NotEmpty(message = "请选择商品")
    private List<CreatePurchaseOrderItem> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreatePurchaseOrderItem {

        @Schema(description = "SKU编码")
        @NotBlank(message = "请选择商品")
        private String skuCode;

        @Schema(description = "数量")
        @NotNull(message = "请指定商品数量")
        @Size(min = 1, max = 999, message = "商品数量超过限制")
        private Integer quantity;

        @Schema(description = "商品单价")
        @NotNull(message = "请指定商品单价")
        private BigDecimal price;

    }

}
