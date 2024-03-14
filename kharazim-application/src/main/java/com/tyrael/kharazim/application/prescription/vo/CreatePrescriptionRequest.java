package com.tyrael.kharazim.application.prescription.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
@Data
public class CreatePrescriptionRequest {

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "诊所（机构）编码")
    private String clinicCode;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "商品明细")
    private List<Product> products;

    @Data
    public static class Product {

        @Schema(description = "sku编码")
        private String skuCode;

        @Schema(description = "数量")
        private Integer quantity;

    }

}
