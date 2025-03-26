package com.tyrael.kharazim.purchase.controller;

import com.tyrael.kharazim.authentication.CurrentPrincipal;
import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.purchase.app.service.CreatePurchaseOrderService;
import com.tyrael.kharazim.purchase.app.service.QueryPurchaseOrderService;
import com.tyrael.kharazim.purchase.app.vo.purchaseorder.CreatePurchaseOrderRequest;
import com.tyrael.kharazim.purchase.app.vo.purchaseorder.PagePurchaseOrderRequest;
import com.tyrael.kharazim.purchase.app.vo.purchaseorder.PurchaseOrderVO;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2024/5/31
 */
@RestController
@RequestMapping("/purchase-order")
@RequiredArgsConstructor
@Tag(name = "采购单管理")
public class PurchaseOrderController {

    private final CreatePurchaseOrderService createPurchaseOrderService;
    private final QueryPurchaseOrderService queryPurchaseOrderService;

    @PostMapping("/create")
    @Operation(summary = "创建采购单")
    public DataResponse<String> create(@RequestBody CreatePurchaseOrderRequest request,
                                       @Schema(hidden = true) @CurrentPrincipal AuthUser currentUser) {
        return DataResponse.success(createPurchaseOrderService.create(request, currentUser));
    }

    @GetMapping("/page")
    @Operation(summary = "采购单分页")
    public PageResponse<PurchaseOrderVO> page(@ParameterObject PagePurchaseOrderRequest pageRequest) {
        return queryPurchaseOrderService.page(pageRequest);
    }

    @GetMapping("/detail/{code}")
    @Operation(summary = "采购单详情")
    public DataResponse<PurchaseOrderVO> detail(@Schema(description = "采购单号") @PathVariable String code) {
        return DataResponse.success(queryPurchaseOrderService.detail(code));
    }

}
