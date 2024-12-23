package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.application.system.dto.requestlog.PageSystemRequestLogRequest;
import com.tyrael.kharazim.application.system.dto.requestlog.SystemEndpointDTO;
import com.tyrael.kharazim.application.system.dto.requestlog.SystemRequestLogDTO;
import com.tyrael.kharazim.application.system.service.SystemRequestLogService;
import com.tyrael.kharazim.common.dto.MultiResponse;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
@Tag(name = "系统日志")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/request-log")
public class SystemRequestLogController {

    private final SystemRequestLogService systemRequestLogService;

    @GetMapping("/latest/{rows}")
    @Operation(description = "获取最新若干条日志", summary = "最新若干条日志")
    public MultiResponse<SystemRequestLogDTO> latestLogs(
            @PathVariable("rows") @Parameter(description = "日志条数", required = true) Integer rows) {
        return MultiResponse.success(systemRequestLogService.latestLogs(rows));
    }

    @GetMapping("/page")
    @Operation(summary = "日志分页")
    public PageResponse<SystemRequestLogDTO> page(@ParameterObject PageSystemRequestLogRequest pageCommand) {
        return systemRequestLogService.page(pageCommand);
    }

    @GetMapping("/endpoints")
    @Operation(summary = "获取所有endpoints")
    public MultiResponse<SystemEndpointDTO> endpoints() {
        return MultiResponse.success(systemRequestLogService.endpoints());
    }

    @PutMapping("/disable/endpoint")
    @Operation(summary = "禁止endpoint记录日志")
    public Response disableEndpointLog(@Parameter(description = "endpoint", required = true)
                                       @NotEmpty(message = "endpoint不能为空") String endpoint) {
        systemRequestLogService.disableEndpointLog(endpoint);
        return Response.success();
    }

    @PutMapping("/enable/endpoint")
    @Operation(summary = "开启endpoint记录日志")
    public Response enableEndpointLog(@Parameter(description = "endpoint", required = true)
                                      @NotEmpty(message = "endpoint不能为空") String endpoint) {
        systemRequestLogService.enableEndpointLog(endpoint);
        return Response.success();
    }

}
