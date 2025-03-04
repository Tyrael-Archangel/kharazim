package com.tyrael.kharazim.basicdata.controller.requestlog;

import com.tyrael.kharazim.base.dto.MultiResponse;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.basicdata.app.dto.requestlog.PageSystemRequestLogRequest;
import com.tyrael.kharazim.basicdata.app.dto.requestlog.SystemRequestLogDTO;
import com.tyrael.kharazim.basicdata.app.service.reqeustlog.SystemRequestLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
