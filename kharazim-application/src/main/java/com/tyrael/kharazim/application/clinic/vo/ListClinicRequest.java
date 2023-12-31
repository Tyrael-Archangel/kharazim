package com.tyrael.kharazim.application.clinic.vo;

import com.tyrael.kharazim.application.clinic.enums.ClinicStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/31
 */
@Data
public class ListClinicRequest {

    @Schema(description = "诊所（机构名称）")
    private String name;

    @Schema(description = "状态")
    private ClinicStatus status;

}
