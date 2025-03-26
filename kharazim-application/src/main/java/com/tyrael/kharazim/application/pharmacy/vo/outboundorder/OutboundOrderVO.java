package com.tyrael.kharazim.application.pharmacy.vo.outboundorder;

import com.tyrael.kharazim.application.pharmacy.enums.OutboundOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutboundOrderVO {

    @Schema(description = "出库单编码")
    private String code;

    @Schema(description = "出库状态")
    private OutboundOrderStatus status;

    @Schema(description = "来源单据编码")
    private String sourceBusinessCode;

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "会员姓名")
    private String customerName;

    @Schema(description = "诊所编码")
    private String clinicCode;

    @Schema(description = "诊所名称")
    private String clinicName;

    @Schema(description = "来源单据备注")
    private String sourceRemark;

    @Schema(description = "出库单商品明细")
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

        @Schema(description = "默认图片链接")
        private String defaultImage;

        @Schema(description = "数量")
        private Integer quantity;

    }

}
