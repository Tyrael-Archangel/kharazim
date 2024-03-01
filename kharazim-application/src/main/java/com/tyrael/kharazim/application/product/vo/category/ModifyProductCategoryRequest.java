package com.tyrael.kharazim.application.product.vo.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/3/1
 */
@Data
public class ModifyProductCategoryRequest {

    @NotBlank(message = "商品分类编码不能为空")
    @Schema(description = "商品分类编码")
    private String code;

    @Schema(description = "商品分类名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

}
