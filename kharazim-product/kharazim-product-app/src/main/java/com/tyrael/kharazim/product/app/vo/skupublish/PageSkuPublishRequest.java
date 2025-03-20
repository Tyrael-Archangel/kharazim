package com.tyrael.kharazim.product.app.vo.skupublish;

import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.product.app.constant.ProductDictConstants;
import com.tyrael.kharazim.product.app.enums.SkuPublishStatus;
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

    /**
     * {@link ProductDictConstants#SKU_PUBLISH_STATUS}
     */
    @Schema(description = "发布状态，字典编码: sku_publish_status", implementation = SkuPublishStatus.class)
    private SkuPublishStatus publishStatus;

    @Schema(description = "商品名称")
    private String skuName;

    @Schema(description = "商品编码")
    private String skuCode;

    @ArraySchema(arraySchema = @Schema(description = "诊所编码"))
    private Set<String> clinicCodes;

}
