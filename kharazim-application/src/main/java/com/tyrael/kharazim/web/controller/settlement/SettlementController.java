package com.tyrael.kharazim.web.controller.settlement;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUser;
import com.tyrael.kharazim.application.settlement.service.SettlementOrderService;
import com.tyrael.kharazim.application.settlement.service.SettlementPayService;
import com.tyrael.kharazim.application.settlement.vo.PageSettlementOrderRequest;
import com.tyrael.kharazim.application.settlement.vo.SettlementOrderVO;
import com.tyrael.kharazim.application.settlement.vo.SettlementPayCommand;
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
                                        @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        settlementPayService.payWithRechargeCard(payCommand, currentUser);
        return Response.success();
    }

}
