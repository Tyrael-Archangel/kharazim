package com.tyrael.kharazim.finance.controller;

import com.tyrael.kharazim.authentication.CurrentPrincipal;
import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.dto.Response;
import com.tyrael.kharazim.finance.app.service.SettlementOrderService;
import com.tyrael.kharazim.finance.app.service.SettlementPayService;
import com.tyrael.kharazim.finance.app.vo.settlement.PageSettlementOrderRequest;
import com.tyrael.kharazim.finance.app.vo.settlement.SettlementOrderVO;
import com.tyrael.kharazim.finance.app.vo.settlement.SettlementPayCommand;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@RestController
@RequestMapping("/settlement-order")
@RequiredArgsConstructor
@Tag(name = "结算单")
public class SettlementController {

    private final SettlementOrderService settlementOrderService;
    private final SettlementPayService settlementPayService;

    @GetMapping("/detail/{code}")
    @Operation(summary = "结算单详情")
    public DataResponse<SettlementOrderVO> detail(@Schema(description = "结算单编码") @PathVariable String code) {
        return DataResponse.success(settlementOrderService.findByCode(code));
    }

    @GetMapping("/page")
    @Operation(summary = "结算单分页")
    public PageResponse<SettlementOrderVO> page(@ParameterObject PageSettlementOrderRequest pageRequest) {
        return settlementOrderService.page(pageRequest);
    }

    @PostMapping("/pay")
    @Operation(summary = "使用储值单结算")
    public Response payWithRechargeCard(@RequestBody @Valid SettlementPayCommand payCommand,
                                        @Schema(hidden = true) @CurrentPrincipal AuthUser currentUser) {
        settlementPayService.payWithRechargeCard(payCommand, currentUser);
        return Response.success();
    }

}
