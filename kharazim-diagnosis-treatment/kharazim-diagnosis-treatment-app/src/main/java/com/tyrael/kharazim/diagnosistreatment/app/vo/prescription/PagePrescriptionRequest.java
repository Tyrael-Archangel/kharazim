package com.tyrael.kharazim.diagnosistreatment.app.vo.prescription;

import com.tyrael.kharazim.base.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
@Data
public class PagePrescriptionRequest extends PageCommand {

    @Schema(description = "处方编码")
    private String prescriptionCode;

    @Schema(description = "会员编码")
    private String customerCode;

    @ArraySchema(arraySchema = @Schema(description = "诊所（机构）编码"))
    private Set<String> clinicCodes;

    @Schema(description = "创建开始日期", format = "yyyy-MM-dd", example = "2024-04-01")
    private LocalDate createDateMin;

    @Schema(description = "创建截止日期", format = "yyyy-MM-dd", example = "2024-04-30")
    private LocalDate createDateMax;

}
