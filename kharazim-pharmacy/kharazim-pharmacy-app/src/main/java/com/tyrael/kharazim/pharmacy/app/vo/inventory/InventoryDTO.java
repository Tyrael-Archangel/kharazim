package com.tyrael.kharazim.pharmacy.app.vo.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {

    @Schema(description = "诊所编码")
    private String clinicCode;

    @Schema(description = "诊所名称")
    private String clinicName;

    @Schema(description = "SKU编码")
    private String skuCode;

    @Schema(description = "SKU名称")
    private String skuName;

    @Schema(description = "商品分类编码")
    private String categoryCode;

    @Schema(description = "商品分类名称")
    private String categoryName;

    @Schema(description = "单位编码")
    private String unitCode;

    @Schema(description = "单位名称")
    private String unitName;

    @Schema(description = "默认图片")
    private String defaultImage;

    @Schema(description = "描述信息")
    private String description;

    @Schema(description = "在库库存数量")
    private Integer quantity;

    @Schema(description = "已预占数量")
    private Integer occupiedQuantity;

    @Schema(description = "可用库存数量")
    private Integer usableQuantity;

}
