package com.tyrael.kharazim.application.system.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictItemOptionDTO {

    @Schema(description = "字典选项的值")
    private String value;

    @Schema(description = "字典选项标签")
    private String label;

}
