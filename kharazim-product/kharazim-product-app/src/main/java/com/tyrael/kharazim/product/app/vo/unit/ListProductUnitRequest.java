package com.tyrael.kharazim.product.app.vo.unit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/2/20
 */
@Data
public class ListProductUnitRequest {

    @Schema(description = "单位名称")
    private String name;

}
