package com.tyrael.kharazim.product.app.vo.sku;

import com.tyrael.kharazim.basicdata.sdk.model.FileVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

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

    @Schema(description = "商品分类全路径名称")
    private String categoryFullName;

    @Schema(description = "供应商编码")
    private String supplierCode;

    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "单位编码")
    private String unitCode;

    @Schema(description = "单位名称")
    private String unitName;

    @Schema(description = "默认图片")
    private String defaultImage;

    @Schema(description = "默认图片链接")
    private String defaultImageUrl;

    @Schema(description = "图片")
    private List<String> images;

    @Schema(description = "图片链接")
    private List<String> imageUrls;

    @Schema(description = "图片链接")
    private List<FileVO> imageFiles;

    @Schema(description = "描述信息")
    private String description;

    @Schema(description = "属性")
    private List<Attribute> attributes;

    @Schema(description = "属性描述")
    private String attributesDesc;

}
