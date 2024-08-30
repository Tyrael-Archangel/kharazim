package com.tyrael.kharazim.application.pharmacy.vo.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2024/8/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryOccupyVO {

    @Schema(description = "业务编码")
    private String businessCode;

    @Schema(description = "SKU编码")
    private String skuCode;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "诊所编码")
    private String clinicCode;

}
