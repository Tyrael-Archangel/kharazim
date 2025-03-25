package com.tyrael.kharazim.purchase.app.vo.supplier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/2/22
 */
@Getter
@Builder
@ToString
public class SupplierDTO {

    @Schema(description = "供应商编码")
    private String code;

    @Schema(description = "供应商名称")
    private String name;

    @Schema(description = "备注信息")
    private String remark;

    @Schema(description = "创建人")
    private String creator;

    @Schema(description = "创建人编码")
    private String creatorCode;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
