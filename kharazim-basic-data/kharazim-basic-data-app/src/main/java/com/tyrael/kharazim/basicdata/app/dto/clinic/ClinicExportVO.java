package com.tyrael.kharazim.basicdata.app.dto.clinic;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import cn.idev.excel.annotation.write.style.ContentRowHeight;
import cn.idev.excel.annotation.write.style.ContentStyle;
import cn.idev.excel.enums.poi.HorizontalAlignmentEnum;
import cn.idev.excel.enums.poi.VerticalAlignmentEnum;
import com.tyrael.kharazim.basicdata.app.enums.ClinicStatus;
import lombok.Builder;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/5/24
 */
@Data
@Builder
@ExcelIgnoreUnannotated
@ContentRowHeight(150)
@ContentStyle(verticalAlignment = VerticalAlignmentEnum.CENTER, horizontalAlignment = HorizontalAlignmentEnum.CENTER)
public class ClinicExportVO {

    @ExcelProperty("诊所编码")
    @ColumnWidth(16)
    private String code;

    @ExcelProperty("诊所图片")
    @ColumnWidth(60)
    private byte[] image;

    @ExcelProperty("诊所名称")
    @ColumnWidth(20)
    private String name;

    @ExcelProperty("诊所英文名称")
    @ColumnWidth(20)
    private String englishName;

    @ExcelProperty("状态")
    @ColumnWidth(15)
    private ClinicStatus status;

}
