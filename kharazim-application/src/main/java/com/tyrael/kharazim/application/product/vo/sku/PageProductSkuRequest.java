package com.tyrael.kharazim.application.product.vo.sku;

import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
@Data
public class PageProductSkuRequest extends PageCommand {

    @Schema(description = "SKU编码")
    private String code;

    @Schema(description = "SKU名称")
    private String name;

    @Schema(description = "商品分类编码")
    private Set<String> categoryCodes;

    @Schema(description = "供应商编码")
    private Set<String> supplierCodes;

    @Schema(description = "描述信息")
    private String description;

}
