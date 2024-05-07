package com.tyrael.kharazim.application.settlement.vo;

import com.tyrael.kharazim.application.settlement.enums.SettlementOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Data
public class SettlementOrderVO {

    @Schema(description = "结算单编码")
    private String code;

    @Schema(description = "结算状态")
    private SettlementOrderStatus status;

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "会员姓名")
    private String customerName;

    @Schema(description = "诊所（机构）编码")
    private String clinicCode;

    @Schema(description = "诊所（机构）")
    private String clinicName;

    @Schema(description = "来源处方")
    private String sourcePrescriptionCode;

    @Schema(description = "总金额（元）")
    private BigDecimal totalAmount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "结算时间")
    private LocalDateTime settlementTime;

    @Schema(description = "商品明细")
    private List<Item> items;

    @Data
    public static class Item {

        @Schema(description = "SKU编码")
        private String skuCode;

        @Schema(description = "SKU名称")
        private String skuName;

        @Schema(description = "商品分类编码")
        private String categoryCode;

        @Schema(description = "商品分类名称")
        private String categoryName;

        @Schema(description = "供应商编码")
        private String supplierCode;

        @Schema(description = "供应商名称")
        private String supplierName;

        @Schema(description = "单位编码")
        private String unitCode;

        @Schema(description = "单位名称")
        private String unitName;

        @Schema(description = "默认图片链接")
        private String defaultImageUrl;

        @Schema(description = "描述信息")
        private String description;

        @Schema(description = "数量")
        private Integer quantity;

        @Schema(description = "单价（元）")
        private BigDecimal price;

        @Schema(description = "商品项金额（元）")
        private BigDecimal amount;
    }

}
