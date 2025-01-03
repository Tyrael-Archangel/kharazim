package com.tyrael.kharazim.web.controller.customer;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUser;
import com.tyrael.kharazim.application.customer.service.CustomerCommunicationLogService;
import com.tyrael.kharazim.application.customer.vo.communication.AddCustomerCommunicationLogRequest;
import com.tyrael.kharazim.application.customer.vo.communication.CustomerCommunicationLogPageRequest;
import com.tyrael.kharazim.application.customer.vo.communication.CustomerCommunicationLogVO;
import com.tyrael.kharazim.common.dto.DataResponse;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
@RestController
@RequestMapping("/customer/communication")
@RequiredArgsConstructor
@Tag(name = "会员沟通记录接口")
public class CustomerCommunicationLogController {

    private final CustomerCommunicationLogService customerCommunicationLogService;

    @GetMapping("/page")
    @Operation(summary = "沟通记录分页查询")
    public PageResponse<CustomerCommunicationLogVO> page(@ParameterObject CustomerCommunicationLogPageRequest pageRequest) {
        return customerCommunicationLogService.page(pageRequest);
    }

    @PostMapping
    @Operation(summary = "新建沟通记录")
    public Response add(@RequestBody @Valid AddCustomerCommunicationLogRequest addRequest,
                        @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        return DataResponse.success(customerCommunicationLogService.add(addRequest, currentUser));
    }
}
