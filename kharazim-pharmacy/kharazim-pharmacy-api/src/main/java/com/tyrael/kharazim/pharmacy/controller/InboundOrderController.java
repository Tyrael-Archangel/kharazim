package com.tyrael.kharazim.pharmacy.controller;

import com.tyrael.kharazim.authentication.CurrentPrincipal;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.dto.Response;
import com.tyrael.kharazim.pharmacy.app.service.InboundOrderService;
import com.tyrael.kharazim.pharmacy.app.vo.inbound.AddInboundRequest;
import com.tyrael.kharazim.pharmacy.app.vo.inbound.InboundOrderVO;
import com.tyrael.kharazim.pharmacy.app.vo.inbound.PageInboundOrderRequest;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2024/8/12
 */
@RestController
@RequestMapping("/inbound-order")
@RequiredArgsConstructor
@Tag(name = "入库单管理")
public class InboundOrderController {

    private final InboundOrderService inboundOrderService;

    @GetMapping("/page")
    @Operation(summary = "入库单数据分页")
    public PageResponse<InboundOrderVO> page(@ParameterObject PageInboundOrderRequest pageRequest) {
        return inboundOrderService.page(pageRequest);
    }

    @PostMapping("/inbound")
    @Operation(summary = "入库")
    public Response inbound(@RequestBody AddInboundRequest inboundRequest,
                            @Schema(hidden = true) @CurrentPrincipal AuthUser currentUser) {
        inboundOrderService.inbound(inboundRequest, currentUser);
        return Response.success();
    }

}
