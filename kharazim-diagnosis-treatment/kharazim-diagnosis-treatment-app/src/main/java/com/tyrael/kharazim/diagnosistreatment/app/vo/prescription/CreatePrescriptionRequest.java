package com.tyrael.kharazim.diagnosistreatment.app.vo.prescription;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
@Data
public class CreatePrescriptionRequest {

    @Schema(description = "会员编码")
    @NotBlank(message = "请指定会员")
    private String customerCode;

    @Schema(description = "诊所（机构）编码")
    @NotBlank(message = "请指定诊所")
    private String clinicCode;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "商品明细")
    @NotEmpty(message = "请指定商品信息")
    private List<Product> products;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "创建处方商品项")
    public static class Product {

        @Schema(description = "SKU编码")
        @NotBlank(message = "请指定商品")
        private String skuCode;

        @Schema(description = "数量")
        @NotNull(message = "请指定商品数量")
        @Size(min = 1, max = 999, message = "商品数量超过限制")
        private Integer quantity;

    }

}
