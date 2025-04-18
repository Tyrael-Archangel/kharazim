package com.tyrael.kharazim.pharmacy.app.vo.inventory;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/8/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListInventoryOfClinicRequest {

    @Schema(description = "诊所编码")
    @NotBlank(message = "请指定诊所")
    private String clinicCode;

    @ArraySchema(schema = @Schema(description = "SKU编码"))
    @NotEmpty(message = "请指定SKU")
    private Set<String> skuCodes;

}
