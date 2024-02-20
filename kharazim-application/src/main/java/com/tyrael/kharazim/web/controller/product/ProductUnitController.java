package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.application.product.service.ProductUnitService;
import com.tyrael.kharazim.application.product.vo.AddProductUnitRequest;
import com.tyrael.kharazim.application.product.vo.ListProductUnitRequest;
import com.tyrael.kharazim.application.product.vo.PageProductUnitRequest;
import com.tyrael.kharazim.application.product.vo.ProductUnitVO;
import com.tyrael.kharazim.common.dto.DataResponse;
import com.tyrael.kharazim.common.dto.MultiResponse;
import com.tyrael.kharazim.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
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
        return DataResponse.ok(productUnitService.add(addUnitRequest));
    }

}
