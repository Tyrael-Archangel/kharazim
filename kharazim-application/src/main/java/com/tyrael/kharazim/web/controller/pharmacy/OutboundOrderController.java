package com.tyrael.kharazim.web.controller.pharmacy;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUser;
import com.tyrael.kharazim.application.pharmacy.service.OutboundOrderService;
import com.tyrael.kharazim.application.pharmacy.vo.outboundorder.OutboundOrderVO;
import com.tyrael.kharazim.application.pharmacy.vo.outboundorder.OutboundRequest;
import com.tyrael.kharazim.application.pharmacy.vo.outboundorder.PageOutboundOrderRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@RestController
@RequestMapping("/outbound-order")
@RequiredArgsConstructor
@Tag(name = "出库单管理")
public class OutboundOrderController {

    private final OutboundOrderService outboundOrderService;

    @GetMapping("/page")
    @Operation(summary = "出库单数据分页")
    public PageResponse<OutboundOrderVO> page(@ParameterObject PageOutboundOrderRequest pageRequest) {
        return outboundOrderService.page(pageRequest);
    }

    @PostMapping("/outbound")
    @Operation(summary = "出库")
    public Response outbound(@RequestBody OutboundRequest outboundRequest,
                             @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        outboundOrderService.outbound(outboundRequest, currentUser);
        return Response.success();
    }

}
