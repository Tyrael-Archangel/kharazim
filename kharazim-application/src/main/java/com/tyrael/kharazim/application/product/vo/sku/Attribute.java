package com.tyrael.kharazim.application.product.vo.sku;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2024/3/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {

    @Schema(description = "属性名")
    private String name;

    @Schema(description = "属性值")
    private String value;

}
