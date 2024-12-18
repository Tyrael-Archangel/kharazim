package com.tyrael.kharazim.application.prescription.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.tyrael.kharazim.common.excel.MergeCell;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/5/27
 */
@Data
@Builder
@ExcelIgnoreUnannotated
@ColumnWidth(16)
public class PrescriptionExportVO {

    @ExcelProperty("处方编码")
    @MergeCell(unique = true)
    @ColumnWidth(20)
    private String code;

    @ExcelProperty("会员姓名")
    @MergeCell
    private String customerName;

    @ExcelProperty("会员编码")
    @MergeCell
    private String customerCode;

    @ExcelProperty("诊所")
    @MergeCell
    private String clinicName;

    @ExcelProperty("总金额")
    @MergeCell
    private BigDecimal totalAmount;

    @ExcelProperty("创建人")
    @MergeCell
    private String creator;

    @ExcelProperty("创建时间")
    @ColumnWidth(22)
    @MergeCell
    private LocalDateTime createTime;

    @ExcelProperty("商品编码")
    private String skuCode;

    @ExcelProperty("商品名称")
    @ColumnWidth(20)
    private String skuName;

    @ExcelProperty("商品分类")
    private String categoryName;

    @ExcelProperty("供应商")
    private String supplierName;

    @ExcelProperty("单位")
    @ColumnWidth(10)
    private String unitName;

    @ExcelProperty("数量")
    @ColumnWidth(10)
    private Integer quantity;

    @ExcelProperty("单价")
    @ColumnWidth(12)
    private BigDecimal price;

    @ExcelProperty("商品项金额")
    private BigDecimal amount;

    @ExcelProperty("备注")
    @MergeCell
    @ColumnWidth(25)
    private String remark;

}
