package com.tyrael.kharazim.application.skupublish.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tyrael.kharazim.application.skupublish.enums.SkuPublishStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/3/26
 */
@Data
public class SkuPublishVO {

    @Schema(description = "商品发布序列号")
    private String code;

    @Schema(description = "商品发布状态")
    private SkuPublishStatus publishStatus;

    @Schema(description = "SKU编码")
    private String skuCode;

    @Schema(description = "SKU名称")
    private String skuName;

    @Schema(description = "诊所")
    private String clinicCode;

    @Schema(description = "诊所")
    private String clinicName;

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

    @Schema(description = "单价")
    private BigDecimal price;

    @Schema(description = "生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime effectBegin;

    @Schema(description = "失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime effectEnd;

}
