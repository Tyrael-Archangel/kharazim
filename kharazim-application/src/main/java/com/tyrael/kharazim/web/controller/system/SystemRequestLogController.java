package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.application.system.dto.requestlog.SystemRequestLogDTO;
import com.tyrael.kharazim.application.system.service.SystemRequestLogService;
import com.tyrael.kharazim.common.dto.MultiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @Operation(description = "获取最新若干条日志", summary = "最新若干条日志")
    @GetMapping("/latest/{rows}")
    public MultiResponse<SystemRequestLogDTO> latestLogs(
            @PathVariable("rows") @Parameter(description = "日志条数", required = true) Integer rows) {
        return MultiResponse.success(systemRequestLogService.latestLogs(rows));
    }

}
