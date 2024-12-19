package com.tyrael.kharazim.application.clinic.vo;

import com.tyrael.kharazim.application.clinic.enums.ClinicStatus;
import com.tyrael.kharazim.application.system.domain.DictConstants;
import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/31
 */
@Data
public class PageClinicRequest extends PageCommand {

    @Schema(description = "诊所（机构名称）")
    private String name;

    /**
     * {@link DictConstants#CLINIC_STATUS}
     */
    @Schema(description = "状态，字典编码: clinic_status")
    private ClinicStatus status;

}