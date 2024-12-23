package com.tyrael.kharazim.application.system.dto.requestlog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2024/12/23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemEndpointDTO {

    @Schema(description = "endpoint")
    private String endpoint;

    @Schema(description = "是否活跃的endpoint")
    private Boolean active;

    @Schema(description = "是否允许记录系统日志")
    private Boolean enableSystemLog;

}
