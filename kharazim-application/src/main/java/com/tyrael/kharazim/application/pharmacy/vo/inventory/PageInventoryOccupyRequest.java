package com.tyrael.kharazim.application.pharmacy.vo.inventory;

import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/8/30
 */
@Data
public class PageInventoryOccupyRequest extends PageCommand {

    @Schema(description = "SKU编码")
    @NotBlank(message = "请指定SKU")
    private String skuCode;

    @Schema(description = "诊所编码")
    @NotBlank(message = "请指定诊所")
    private String clinicCode;

}
