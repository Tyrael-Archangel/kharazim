package com.tyrael.kharazim.product.controller;

import com.tyrael.kharazim.authentication.CurrentPrincipal;
import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.dto.Response;
import com.tyrael.kharazim.product.app.service.SkuPublishService;
import com.tyrael.kharazim.product.app.vo.skupublish.PageSkuPublishRequest;
import com.tyrael.kharazim.product.app.vo.skupublish.PublishSkuRequest;
import com.tyrael.kharazim.product.app.vo.skupublish.SkuPublishVO;
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
 * @since 2024/3/26
 */
@RestController
@RequestMapping("/product/publish")
@RequiredArgsConstructor
@Tag(name = "商品发布")
public class SkuPublishController {

    private final SkuPublishService skuPublishService;

    @GetMapping("/page")
    @Operation(summary = "商品发布数据分页")
    public PageResponse<SkuPublishVO> page(@ParameterObject PageSkuPublishRequest pageRequest) {
        return skuPublishService.page(pageRequest);
    }

    @PostMapping("/do-publish")
    @Operation(summary = "发布商品")
    public DataResponse<String> publish(@RequestBody @Valid PublishSkuRequest publishRequest) {
        return DataResponse.success(skuPublishService.publish(publishRequest));
    }

    @PostMapping("/cancel-publish/{code}")
    @Operation(summary = "取消发布商品")
    public Response cancelPublish(@Schema(description = "商品发布序列号") @PathVariable String code,
                                  @Schema(hidden = true) @CurrentPrincipal AuthUser currentUser) {
        skuPublishService.cancelPublish(code, currentUser);
        return Response.success();
    }

}
