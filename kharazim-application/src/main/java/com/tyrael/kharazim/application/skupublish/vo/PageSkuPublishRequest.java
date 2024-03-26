package com.tyrael.kharazim.application.skupublish.vo;

import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/3/26
 */
@Data
public class PageSkuPublishRequest extends PageCommand {

    @Schema(description = "SKU编码")
    private String skuCode;

    @Schema(description = "诊所编码")
    private String clinicCode;

}
