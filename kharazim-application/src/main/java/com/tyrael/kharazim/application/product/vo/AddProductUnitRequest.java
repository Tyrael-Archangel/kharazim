package com.tyrael.kharazim.application.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/2/20
 */
@Data
public class AddProductUnitRequest {

    @Schema(description = "单位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "请输入单位名称")
    private String name;

    @Schema(description = "单位名称")
    private String englishName;

}
