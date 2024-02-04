package com.tyrael.kharazim.web.controller.recharge;

import com.tyrael.kharazim.application.recharge.service.CustomerRechargeCardService;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeCardPageRequest;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeCardVO;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
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

}
