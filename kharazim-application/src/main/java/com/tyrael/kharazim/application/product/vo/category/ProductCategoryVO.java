package com.tyrael.kharazim.application.product.vo.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/4/24
 */
@Data
@Builder
public class ProductCategoryVO {

    @Schema(description = "商品分类编码")
    private String code;

    @Schema(description = "商品分类名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "全路径名称")
    private String fullPathName;

}
