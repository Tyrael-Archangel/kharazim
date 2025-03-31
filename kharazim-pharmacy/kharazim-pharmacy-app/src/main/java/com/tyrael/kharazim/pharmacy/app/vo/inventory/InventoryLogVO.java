package com.tyrael.kharazim.pharmacy.app.vo.inventory;

import com.tyrael.kharazim.pharmacy.app.enums.InventoryChangeTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/8/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryLogVO {

    @Schema(description = "库存流水ID")
    private Long id;

    @Schema(description = "关联业务编码")
    private String businessCode;

    @Schema(description = "批次序列号")
    private String serialCode;

    @Schema(description = "SKU编码")
    private String skuCode;

    @Schema(description = "SKU名称")
    private String skuName;

    @Schema(description = "商品分类编码")
    private String categoryCode;

    @Schema(description = "商品分类名称")
    private String categoryName;

    @Schema(description = "单位编码")
    private String unitCode;

    @Schema(description = "单位名称")
    private String unitName;

    @Schema(description = "默认图片")
    private String defaultImage;

    @Schema(description = "描述信息")
    private String description;

    @Schema(description = "变化数量")
    private Integer quantity;

    @Schema(description = "结存数量")
    private Integer balanceQuantity;

    @Schema(description = "结存预占数量")
    private Integer balanceOccupyQuantity;

    @Schema(description = "诊所编码")
    private String clinicCode;

    @Schema(description = "诊所名称")
    private String clinicName;

    @Schema(description = "库存变化类型")
    private InventoryChangeTypeEnum changeType;

    @Schema(description = "操作时间")
    private LocalDateTime operateTime;

    @Schema(description = "操作人")
    private String operator;

    @Schema(description = "操作人编码")
    private String operatorCode;

}
