package com.tyrael.kharazim.pharmacy.app.vo.inbound;

import com.tyrael.kharazim.pharmacy.app.enums.InboundOrderSourceType;
import com.tyrael.kharazim.pharmacy.app.enums.InboundOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/8/8
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InboundOrderVO {

    @Schema(description = "入库单编码")
    private String code;

    @Schema(description = "来源业务编码")
    private String sourceBusinessCode;

    @Schema(description = "诊所编码")
    private String clinicCode;

    @Schema(description = "诊所")
    private String clinicName;

    @Schema(description = "供应商编码")
    private String supplierCode;

    @Schema(description = "供应商")
    private String supplierName;

    @Schema(description = "来源备注")
    private String sourceRemark;

    @Schema(description = "来源类型")
    private InboundOrderSourceType sourceType;

    @Schema(description = "入库状态")
    private InboundOrderStatus status;

    @Schema(description = "商品明细数据")
    private List<Item> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {

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

        @Schema(description = "数量")
        private Integer quantity;

        @Schema(description = "已入库数量")
        private Integer inboundedQuantity;

        @Schema(description = "待入库数量")
        private Integer remainQuantity;
    }

}
