package com.tyrael.kharazim.web.controller.recharge;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUser;
import com.tyrael.kharazim.application.recharge.service.RechargeCardTypeService;
import com.tyrael.kharazim.application.recharge.vo.*;
import com.tyrael.kharazim.common.dto.DataResponse;
import com.tyrael.kharazim.common.dto.MultiResponse;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.dto.Response;
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
        return DataResponse.success(rechargeCardTypeService.create(addRequest));
    }

    @PostMapping("/modify")
    @Operation(summary = "修改储值卡项")
    public Response modify(@RequestBody @Valid ModifyRechargeCardTypeRequest modifyRequest,
                           @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        rechargeCardTypeService.modify(modifyRequest, currentUser);
        return Response.success();
    }

    @PutMapping("/disable-create-new-card/{code}")
    @Operation(summary = "禁止发卡")
    public Response disableCreateNewCard(
            @PathVariable("code") @Parameter(description = "储值卡项编码", required = true) String code,
            @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        rechargeCardTypeService.disableCreateNewCard(code, currentUser);
        return Response.success();
    }

    @PutMapping("/enable-create-new-card/{code}")
    @Operation(summary = "启用发卡")
    public Response enableCreateNewCard(
            @PathVariable("code") @Parameter(description = "储值卡项编码", required = true) String code,
            @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        rechargeCardTypeService.enableCreateNewCard(code, currentUser);
        return Response.success();
    }


    @GetMapping("/page")
    @Operation(summary = "储值卡项分页")
    public PageResponse<RechargeCardTypeVO> page(@ParameterObject PageRechargeCardTypeRequest pageRequest) {
        return rechargeCardTypeService.page(pageRequest);
    }

    @GetMapping("list")
    @Operation(summary = "储值卡项列表")
    public MultiResponse<RechargeCardTypeVO> list(@ParameterObject ListRechargeCardTypeRequest listRequest) {
        return MultiResponse.success(rechargeCardTypeService.list(listRequest));
    }

}
