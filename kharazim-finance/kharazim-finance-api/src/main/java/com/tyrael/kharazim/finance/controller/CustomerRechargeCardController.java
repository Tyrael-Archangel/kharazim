package com.tyrael.kharazim.finance.controller;

import com.tyrael.kharazim.authentication.CurrentPrincipal;
import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.base.dto.MultiResponse;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.dto.Response;
import com.tyrael.kharazim.finance.app.service.CustomerRechargeCardService;
import com.tyrael.kharazim.finance.app.vo.recharge.*;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @PutMapping("/mark-paid/{code}")
    @Operation(summary = "更新储值单为已收款")
    public Response markPaid(
            @PathVariable("code") @Parameter(description = "储值单号", required = true) String code,
            @Schema(hidden = true) @CurrentPrincipal AuthUser currentUser) {
        customerRechargeCardService.markPaid(code, currentUser);
        return Response.success();
    }

    @PostMapping("/chargeback")
    @Operation(summary = "退卡")
    public Response chargeback(@RequestBody @Valid CustomerRechargeCardChargebackRequest chargebackRequest,
                               @Schema(hidden = true) @CurrentPrincipal AuthUser currentUser) {
        customerRechargeCardService.chargeback(chargebackRequest, currentUser);
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

    @GetMapping("/page-log/{code}")
    @Operation(summary = "储值单分页日志记录")
    public PageResponse<CustomerRechargeCardLogVO> pageRechargeCardLog(
            @PathVariable("code") @Parameter(description = "储值单号", required = true) String code,
            @ParameterObject PageCustomerRechargeCardLogRequest pageCommand) {
        return customerRechargeCardService.pageRechargeCardLog(code, pageCommand);
    }

    @PutMapping("/mark-refunded/{code}")
    @Operation(summary = "更新储值单为已退款")
    public Response markRefunded(
            @PathVariable("code") @Parameter(description = "储值单号", required = true) String code,
            @Schema(hidden = true) @CurrentPrincipal AuthUser currentUser) {
        customerRechargeCardService.markRefunded(code, currentUser);
        return Response.success();
    }

    @GetMapping("/customer/balance-overview/{customerCode}")
    @Operation(summary = "会员账户金额总览", description = "会员账户金额总览")
    public DataResponse<CustomerBalanceOverviewVO> balanceOverview(
            @PathVariable("customerCode") @Parameter(description = "会员编码") String customerCode) {
        return DataResponse.success(customerRechargeCardService.customerBalanceOverview(customerCode));
    }

    @GetMapping("/customer/card-type-balance/{customerCode}")
    @Operation(summary = "会员储值卡项剩余金额统计")
    public MultiResponse<CustomerRechargeCardTypeBalanceVO> rechargeCardBalance(
            @PathVariable("customerCode") @Parameter(description = "会员编码") String customerCode) {
        return MultiResponse.success(customerRechargeCardService.customerRechargeCardTypeBalance(customerCode));
    }

}
