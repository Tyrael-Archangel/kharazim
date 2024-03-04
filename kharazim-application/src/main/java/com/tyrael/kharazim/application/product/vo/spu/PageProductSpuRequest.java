package com.tyrael.kharazim.application.product.vo.spu;

import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
@Data
public class PageProductSpuRequest extends PageCommand {

    @Schema(description = "SPU名称")
    private String name;

    @Schema(description = "商品分类编码")
    private String categoryCode;

    @Schema(description = "供应商编码")
    private String supplierCode;

    @Schema(description = "描述信息")
    private String description;

}
