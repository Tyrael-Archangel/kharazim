package com.tyrael.kharazim.web.controller.purchase;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUser;
import com.tyrael.kharazim.application.purchase.service.PurchaseOrderService;
import com.tyrael.kharazim.application.purchase.vo.PurchaseOrderVO;
import com.tyrael.kharazim.application.purchase.vo.request.CreatePurchaseOrderRequest;
import com.tyrael.kharazim.common.dto.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping("/create")
    @Operation(summary = "创建采购单")
    public DataResponse<String> create(@RequestBody CreatePurchaseOrderRequest request,
                                       @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        return DataResponse.ok(purchaseOrderService.create(request, currentUser));
    }

    @GetMapping("/detail/{code}")
    @Operation(summary = "采购单详情")
    public DataResponse<PurchaseOrderVO> detail(@Schema(description = "采购单号") @PathVariable String code) {
        return DataResponse.ok(purchaseOrderService.detail(code));
    }

}
