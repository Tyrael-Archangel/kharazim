package com.tyrael.kharazim.product.app.vo.sku;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
@Data
public class AddProductRequest {

    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "商品名称不能为空")
    private String name;

    @Schema(description = "商品分类编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "请指定商品分类")
    private String categoryCode;

    @Schema(description = "供应商编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "请指定供应商")
    private String supplierCode;

    @Schema(description = "单位编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "请指定单位")
    private String unitCode;

    @Schema(description = "默认图片")
    private String defaultImage;

    @Schema(description = "图片")
    private List<String> images;

    @Schema(description = "描述信息")
    private String description;

    @Schema(description = "属性")
    private List<Attribute> attributes;

}
