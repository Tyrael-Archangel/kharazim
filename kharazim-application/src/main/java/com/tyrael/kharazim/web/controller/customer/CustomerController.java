package com.tyrael.kharazim.web.controller.customer;

import com.tyrael.kharazim.application.customer.service.CustomerService;
import com.tyrael.kharazim.application.customer.vo.CustomerBaseVO;
import com.tyrael.kharazim.common.dto.DataResponse;
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
 * @since 2024/1/8
 */
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Tag(name = "会员接口")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{code}")
    @Operation(summary = "查询会员基本信息")
    public DataResponse<CustomerBaseVO> findByCode(
            @PathVariable("code") @Parameter(description = "会员编码", required = true) String code) {
        return DataResponse.ok(customerService.findByCode(code));
    }

}
