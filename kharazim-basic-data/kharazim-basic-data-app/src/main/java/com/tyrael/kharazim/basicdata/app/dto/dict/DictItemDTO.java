package com.tyrael.kharazim.basicdata.app.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@Builder
public class DictItemDTO {

    @Schema(description = "字典项ID")
    private Long id;

    @Schema(description = "字典编码")
    private String dictCode;

    @Schema(description = "字典项键")
    private String key;

    @Schema(description = "字典项值")
    private String value;

    @Schema(description = "排序")
    private Integer sort;

}
