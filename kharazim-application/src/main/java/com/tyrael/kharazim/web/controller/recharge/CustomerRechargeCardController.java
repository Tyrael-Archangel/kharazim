package com.tyrael.kharazim.web.controller.recharge;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUser;
import com.tyrael.kharazim.application.recharge.service.CustomerRechargeCardService;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeRequest;
import com.tyrael.kharazim.common.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Response recharge(@RequestBody @Valid CustomerRechargeRequest rechargeRequest,
                             @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        customerRechargeCardService.recharge(rechargeRequest, currentUser);
        return Response.success();
    }

}
