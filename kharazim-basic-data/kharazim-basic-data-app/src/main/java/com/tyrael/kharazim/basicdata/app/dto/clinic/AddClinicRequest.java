package com.tyrael.kharazim.basicdata.app.dto.clinic;

import com.tyrael.kharazim.basicdata.app.enums.ClinicStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/30
 */
@Data
public class AddClinicRequest {

    @Schema(description = "诊所（机构）名称", maxLength = 128)
    @NotBlank(message = "诊所（机构）名称不能为空")
    @Size(max = 128, message = "诊所（机构）名称超长")
    private String name;

    @Schema(description = "诊所（机构）英文名称", maxLength = 128)
    @Size(max = 128, message = "诊所（机构）英文名称超长")
    private String englishName;

    @Schema(description = "图片")
    private String image;

    @Schema(description = "状态", defaultValue = "NORMAL")
    private ClinicStatus status;

    @Schema(hidden = true)
    public ClinicStatus getStatusOrDefault() {
        return status == null ? ClinicStatus.NORMAL : status;
    }

}
