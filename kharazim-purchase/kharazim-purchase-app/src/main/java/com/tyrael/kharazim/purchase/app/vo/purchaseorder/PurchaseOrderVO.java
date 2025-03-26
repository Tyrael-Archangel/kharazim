package com.tyrael.kharazim.purchase.app.vo.purchaseorder;

import com.tyrael.kharazim.purchase.app.enums.PurchasePaymentStatus;
import com.tyrael.kharazim.purchase.app.enums.PurchaseReceiveStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/5/31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderVO {

    @Schema(description = "采购单编码")
    private String code;

    @Schema(description = "诊所编码")
    private String clinicCode;

    @Schema(description = "诊所")
    private String clinicName;

    @Schema(description = "供应商编码")
    private String supplierCode;

    @Schema(description = "供应商")
    private String supplierName;

    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    @Schema(description = "已结算金额")
    private BigDecimal paidAmount;

    @Schema(description = "收货状态")
    private PurchaseReceiveStatus receiveStatus;

    @Schema(description = "结算状态")
    private PurchasePaymentStatus paymentStatus;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "采购商品")
    private List<PurchaseOrderItemVO> items;

    @Schema(description = "支付记录")
    private List<PurchaseOrderPaymentRecordVO> paymentRecords;

    @Schema(description = "收货记录")
    private List<PurchaseOrderReceiveRecordVO> receiveRecords;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private String creator;

    @Schema(description = "创建人编码")
    private String creatorCode;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseOrderItemVO {

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

        @Schema(description = "描述信息")
        private String description;

        @Schema(description = "采购数量")
        private Integer quantity;

        @Schema(description = "已收货数量")
        private Integer receivedQuantity;

        @Schema(description = "单价（元）")
        private BigDecimal price;

        @Schema(description = "商品项金额（元）")
        private BigDecimal amount;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseOrderPaymentRecordVO {

        @Schema(description = "流水号")
        private String serialCode;

        @Schema(description = "支付金额")
        private BigDecimal amount;

        @Schema(description = "支付时间")
        private LocalDateTime paymentTime;

        @Schema(description = "支付用户")
        private String paymentUser;

        @Schema(description = "支付用户编码")
        private String paymentUserCode;

        @Schema(description = "支付凭证")
        private List<String> vouchers;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseOrderReceiveRecordVO {

        @Schema(description = "流水号")
        private String serialCode;

        @Schema(description = "物流跟踪号")
        private String trackingNumber;

        @Schema(description = "收货人")
        private String receiveUser;

        @Schema(description = "收货人编码")
        private String receiveUserCode;

        @Schema(description = "收货时间")
        private LocalDateTime receiveTime;

        @Schema(description = "收货商品明细")
        private List<ReceiveRecordItemVO> receiveItems;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceiveRecordItemVO {

        @Schema(description = "收货SKU编码")
        private String skuCode;

        @Schema(description = "SKU名称")
        private String skuName;

        @Schema(description = "收货数量")
        private Integer quantity;

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

    }

}
