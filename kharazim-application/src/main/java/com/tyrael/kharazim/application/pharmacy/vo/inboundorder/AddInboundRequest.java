package com.tyrael.kharazim.application.pharmacy.vo.inboundorder;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/8/8
 */
@Data
public class AddInboundRequest {

    @Schema(description = "入库单编码")
    @NotBlank(message = "请指定入库单")
    private String inboundOrderCode;

    @Schema(description = "入库商品明细")
    @NotEmpty(message = "请指定入库商品")
    private List<InboundItem> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InboundItem {

        @Schema(description = "SKU编码")
        @NotBlank(message = "请指定商品")
        private String skuCode;

        @Schema(description = "入库数量")
        @NotNull(message = "请指定商品入库数量")
        @Positive(message = "商品入库数量有误")
        private Integer quantity;

    }

}
