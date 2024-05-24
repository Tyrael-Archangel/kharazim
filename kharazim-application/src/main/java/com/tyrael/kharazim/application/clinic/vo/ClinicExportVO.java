package com.tyrael.kharazim.application.clinic.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.tyrael.kharazim.application.clinic.enums.ClinicStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2024/5/24
 */
@Data
@Builder
@ExcelIgnoreUnannotated
public class ClinicExportVO {

    @ExcelProperty("诊所编码")
    private String code;

    @ExcelProperty("诊所图片")
    private byte[] image;

    @ExcelProperty("诊所名称")
    private String name;

    @ExcelProperty("诊所英文名称")
    private String englishName;

    @ExcelProperty("状态")
    private ClinicStatus status;

}
