package com.tyrael.kharazim.web.controller.recharge;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUser;
import com.tyrael.kharazim.application.recharge.service.RechargeCardTypeService;
import com.tyrael.kharazim.application.recharge.vo.AddRechargeCardTypeRequest;
import com.tyrael.kharazim.application.recharge.vo.ModifyRechargeCardTypeRequest;
import com.tyrael.kharazim.common.dto.DataResponse;
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
 * @since 2024/1/25
 */
@RestController
@RequestMapping("/recharge-card-type")
@RequiredArgsConstructor
@Tag(name = "储值卡项")
public class RechargeCardTypeController {

    private final RechargeCardTypeService rechargeCardTypeService;

    @PostMapping
    @Operation(summary = "新建储值卡项")
    public DataResponse<String> create(@RequestBody @Valid AddRechargeCardTypeRequest addRequest) {
        return DataResponse.ok(rechargeCardTypeService.create(addRequest));
    }

    @PostMapping("/modify")
    @Operation(summary = "修改储值卡项")
    public Response modify(@RequestBody @Valid ModifyRechargeCardTypeRequest modifyRequest,
                           @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        rechargeCardTypeService.modify(modifyRequest, currentUser);
        return Response.success();
    }

}
