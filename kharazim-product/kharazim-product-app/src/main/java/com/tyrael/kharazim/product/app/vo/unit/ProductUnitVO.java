package com.tyrael.kharazim.product.app.vo.unit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Tyrael Archangel
 * @since 2024/2/20
 */
@Getter
@Builder
@ToString
public class ProductUnitVO {

    @Schema(description = "单位编码")
    private String code;

    @Schema(description = "单位名称")
    private String name;

    @Schema(description = "单位英文名称")
    private String englishName;

}