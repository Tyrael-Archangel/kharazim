package com.tyrael.kharazim.application.product.vo.sku;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
@Data
public class ProductSkuVO {

    @Schema(description = "SKU编码")
    private String code;

    @Schema(description = "SKU名称")
    private String name;

    @Schema(description = "商品分类编码")
    private String categoryCode;

    @Schema(description = "商品分类名称")
    private String categoryName;

    @Schema(description = "供应商编码")
    private String supplierCode;

    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "默认图片")
    private String defaultImage;

    @Schema(description = "描述信息")
    private String description;

}
