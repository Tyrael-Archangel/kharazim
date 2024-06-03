package com.tyrael.kharazim.application.pharmacy.vo.inventory;

import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Data
public class PageInventoryRequest extends PageCommand {

    @Schema(description = "SKU编码")
    private String skuCode;

    @Schema(description = "商品名称")
    private String skuName;

    @Schema(description = "诊所编码")
    private List<String> clinicCodes;

}
