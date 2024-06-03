package com.tyrael.kharazim.application.skupublish.vo;

import com.tyrael.kharazim.application.skupublish.enums.SkuPublishStatus;
import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/3/26
 */
@Data
public class PageSkuPublishRequest extends PageCommand {

    @Schema(description = "发布状态", implementation = SkuPublishStatus.class)
    private SkuPublishStatus publishStatus;

    @Schema(description = "商品名称")
    private String skuName;

    @ArraySchema(arraySchema = @Schema(description = "诊所编码"))
    private Set<String> clinicCodes;

}
