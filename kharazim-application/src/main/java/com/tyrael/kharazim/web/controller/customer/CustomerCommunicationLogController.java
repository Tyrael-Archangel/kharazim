package com.tyrael.kharazim.web.controller.customer;

import com.tyrael.kharazim.application.customer.service.CustomerCommunicationLogService;
import com.tyrael.kharazim.application.customer.vo.communication.CustomerCommunicationLogPageRequest;
import com.tyrael.kharazim.application.customer.vo.communication.CustomerCommunicationLogVO;
import com.tyrael.kharazim.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
