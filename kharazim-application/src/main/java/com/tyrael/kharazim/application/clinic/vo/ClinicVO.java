package com.tyrael.kharazim.application.clinic.vo;

import com.tyrael.kharazim.application.clinic.enums.ClinicStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2023/12/31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicVO {

    @Schema(description = "诊所（机构）编码")
    private String code;

    @Schema(description = "诊所（机构）名称")
    private String name;

    @Schema(description = "诊所（机构）英文名称")
    private String englishName;

    @Schema(description = "诊所（机构）图片")
    private String image;

    @Schema(description = "状态")
    private ClinicStatus status;

}
