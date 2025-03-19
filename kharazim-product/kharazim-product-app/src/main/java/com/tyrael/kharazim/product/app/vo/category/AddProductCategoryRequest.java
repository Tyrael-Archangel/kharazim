package com.tyrael.kharazim.product.app.vo.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/2/27
 */
@Data
public class AddProductCategoryRequest {

    @Schema(description = "父级分类ID")
    private Long parentId;

    @Schema(description = "分类名称")
    @NotBlank(message = "分类名称不能为空")
    private String name;

    @Schema(description = "备注")
    private String remark;

}
