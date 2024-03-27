package com.tyrael.kharazim.application.prescription.vo;

import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
@Data
public class PagePrescriptionRequest extends PageCommand {

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "诊所（机构）编码")
    private String clinicCode;

    @Schema(description = "创建开始日期")
    private LocalDate createDateMin;

    @Schema(description = "创建截止日期")
    private LocalDate createDateMax;

}
