package com.tyrael.kharazim.basicdata.app.dto.requestlog;

import com.tyrael.kharazim.base.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/12/20
 */
@Data
public class PageSystemRequestLogRequest extends PageCommand {

    @Schema(description = "响应状态码")
    private Integer responseStatus;

    @Schema(description = "endpoint")
    private String endpoint;

    @Schema(description = "请求用户编码")
    private String userCode;

    @Schema(description = "请求开始时间范围-begin")
    private LocalDateTime startTimeRangeBegin;
    @Schema(description = "请求开始时间范围-end")
    private LocalDateTime startTimeRangeEnd;

    @Schema(description = "请求结束时间范围-begin")
    private LocalDateTime endTimeRangeBegin;
    @Schema(description = "请求结束时间范围-end")
    private LocalDateTime endTimeRangeEnd;

}
