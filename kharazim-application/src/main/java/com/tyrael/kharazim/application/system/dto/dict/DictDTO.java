package com.tyrael.kharazim.application.system.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
public class DictDTO {

    @Schema(description = "字典ID")
    private Long id;

    @Schema(description = "字典编码")
    private String code;

    @Schema(description = "字典名")
    private String name;

    @Schema(description = "字典描述信息")
    private String remark;

    @Schema(description = "类型状态：1->启用;0->禁用")
    private Integer status;

    @Schema(description = "创建人")
    private String creator;
    @Schema(description = "创建人编号")
    private String creatorCode;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新人")
    private String updater;
    @Schema(description = "更新人编码")
    private String updaterCode;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "是否允许修改字典项")
    private Boolean allowModifyItem;

    @Schema(hidden = true)
    public void setStatusByEnable(Boolean enable) {
        this.status = Boolean.TRUE.equals(enable) ? 1 : 0;
    }

}
