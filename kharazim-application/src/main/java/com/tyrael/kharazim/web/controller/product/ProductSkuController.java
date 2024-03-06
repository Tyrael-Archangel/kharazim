package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.application.product.service.ProductSkuService;
import com.tyrael.kharazim.application.product.vo.sku.AddProductRequest;
import com.tyrael.kharazim.application.product.vo.sku.PageProductSkuRequest;
import com.tyrael.kharazim.application.product.vo.sku.ProductSkuVO;
import com.tyrael.kharazim.common.dto.DataResponse;
import com.tyrael.kharazim.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2024/3/1
 */
@RestController
@RequestMapping("/product/sku")
@RequiredArgsConstructor
@Tag(name = "商品")
public class ProductSkuController {

    private final ProductSkuService productSkuService;

    @GetMapping("/{code}")
    @Operation(summary = "商品信息")
    public DataResponse<ProductSkuVO> getByCode(@PathVariable("code") String code) {
        return DataResponse.ok(productSkuService.getByCode(code));
    }

    @PostMapping("/create")
    @Operation(summary = "创建商品")
    public DataResponse<String> create(@RequestBody @Valid AddProductRequest addRequest) {
        return DataResponse.ok(productSkuService.create(addRequest));
    }

    @GetMapping("/page")
    @Operation(summary = "商品分页")
    public PageResponse<ProductSkuVO> page(@ParameterObject PageProductSkuRequest pageRequest) {
        return productSkuService.page(pageRequest);
    }

}
