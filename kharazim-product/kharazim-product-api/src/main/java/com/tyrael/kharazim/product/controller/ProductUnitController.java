package com.tyrael.kharazim.product.controller;

import com.tyrael.kharazim.authentication.CurrentPrincipal;
import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.base.dto.MultiResponse;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.dto.Response;
import com.tyrael.kharazim.product.app.service.ProductUnitService;
import com.tyrael.kharazim.product.app.vo.unit.*;
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
 * @since 2024/2/20
 */
@RestController
@RequestMapping("/product/unit")
@RequiredArgsConstructor
@Tag(name = "商品单位")
public class ProductUnitController {

    private final ProductUnitService productUnitService;

    @GetMapping("/page")
    @Operation(summary = "商品单位分页查询")
    public PageResponse<ProductUnitVO> page(@ParameterObject PageProductUnitRequest pageRequest) {
        return productUnitService.page(pageRequest);
    }

    @GetMapping("/list")
    @Operation(summary = "商品单位列表数据", description = "商品单位列表数据，不分页，返回所有符合条件的数据")
    public MultiResponse<ProductUnitVO> list(@ParameterObject ListProductUnitRequest listRequest) {
        return MultiResponse.success(productUnitService.list(listRequest));
    }

    @PostMapping("/add")
    @Operation(summary = "新建商品单位", description = "新建商品单位，返回单位编码")
    public DataResponse<String> add(@RequestBody @Valid AddProductUnitRequest addUnitRequest) {
        return DataResponse.success(productUnitService.add(addUnitRequest));
    }

    @PostMapping("/modify")
    @Operation(summary = "修改商品单位", description = "只能修改单位的英文名称")
    public Response modify(@RequestBody @Valid ModifyProductUnitRequest modifyUnitRequest,
                           @Schema(hidden = true) @CurrentPrincipal AuthUser currentUser) {
        productUnitService.modify(modifyUnitRequest, currentUser);
        return Response.success();
    }

}
