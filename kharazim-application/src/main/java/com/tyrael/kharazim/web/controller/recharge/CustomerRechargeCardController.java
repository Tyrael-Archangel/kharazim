package com.tyrael.kharazim.web.controller.recharge;

import com.tyrael.kharazim.application.recharge.service.CustomerRechargeCardService;
import com.tyrael.kharazim.application.recharge.vo.*;
import com.tyrael.kharazim.common.dto.MultiResponse;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2024/2/1
 */
@RestController
@RequestMapping("/recharge-card")
@RequiredArgsConstructor
@Tag(name = "储值单")
public class CustomerRechargeCardController {

    private final CustomerRechargeCardService customerRechargeCardService;

    @PostMapping("/recharge")
    @Operation(summary = "创建储值单")
    public Response recharge(@RequestBody @Valid CustomerRechargeRequest rechargeRequest) {
        customerRechargeCardService.recharge(rechargeRequest);
        return Response.success();
    }

    @GetMapping("/page")
    @Operation(summary = "会员储值单分页")
    public PageResponse<CustomerRechargeCardVO> rechargeCardPage(
            @ParameterObject @Valid CustomerRechargeCardPageRequest pageRequest) {
        return customerRechargeCardService.page(pageRequest);
    }

    @GetMapping("/list-effective/{customerCode}")
    @Operation(summary = "会员有效的储值单")
    public MultiResponse<CustomerRechargeCardVO> listCustomerEffective(
            @PathVariable("customerCode") @Parameter(description = "会员编码", required = true) String customerCode) {
        return MultiResponse.success(customerRechargeCardService.listCustomerEffective(customerCode));
    }

    @PostMapping("/page-log/{code}")
    @Operation(summary = "储值单分页日志记录")
    public PageResponse<CustomerRechargeCardLogVO> pageRechargeCardLog(
            @PathVariable("code") @Parameter(description = "储值单号", required = true) String code,
            @ParameterObject PageCustomerRechargeCardLogRequest pageCommand) {
        return customerRechargeCardService.pageRechargeCardLog(code, pageCommand);
    }

}
