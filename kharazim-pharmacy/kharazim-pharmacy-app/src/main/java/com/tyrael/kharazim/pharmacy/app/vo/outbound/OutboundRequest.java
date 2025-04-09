package com.tyrael.kharazim.pharmacy.app.vo.outbound;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@Data
public class OutboundRequest {

    @Schema(description = "出库单编码")
    @NotBlank(message = "请指定出库单")
    private String outboundOrderCode;

}
