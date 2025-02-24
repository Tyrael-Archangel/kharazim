package com.tyrael.kharazim.basicdata.app.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
public class DictDTO {

    @Schema(description = "字典编码")
    private String code;

    @Schema(description = "字典描述信息")
    private String desc;

    @Schema(description = "是否允许修改字典项")
    private Boolean allowModifyItem;

}
