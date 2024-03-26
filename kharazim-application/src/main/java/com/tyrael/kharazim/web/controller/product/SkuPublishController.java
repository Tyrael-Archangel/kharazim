package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.application.skupublish.service.SkuPublishService;
import com.tyrael.kharazim.application.skupublish.vo.PageSkuPublishRequest;
import com.tyrael.kharazim.application.skupublish.vo.PublishSkuRequest;
import com.tyrael.kharazim.application.skupublish.vo.SkuPublishVO;
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
 * @since 2024/3/26
 */
@RestController
@RequestMapping("/product/publish")
@RequiredArgsConstructor
@Tag(name = "商品发布")
public class SkuPublishController {

    private final SkuPublishService skuPublishService;

    @GetMapping("/page-effect")
    @Operation(summary = "生效的商品发布数据分页")
    public PageResponse<SkuPublishVO> pageEffect(@ParameterObject PageSkuPublishRequest pageRequest) {
        return skuPublishService.pageEffect(pageRequest);
    }

    @PostMapping("/do-publish")
    @Operation(summary = "发布商品")
    public DataResponse<String> publish(@RequestBody @Valid PublishSkuRequest publishRequest) {
        return DataResponse.ok(skuPublishService.publish(publishRequest));
    }

    @PostMapping("/cancel-publish/{code}")
    @Operation(summary = "取消发布商品")
    public Response cancelPublish(@Schema(description = "商品发布序列号") @PathVariable String code) {
        skuPublishService.cancelPublish(code);
        return Response.success();
    }

}
