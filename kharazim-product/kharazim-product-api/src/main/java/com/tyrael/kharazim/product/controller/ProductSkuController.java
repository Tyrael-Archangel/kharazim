package com.tyrael.kharazim.product.controller;

import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.product.app.service.ProductSkuService;
import com.tyrael.kharazim.product.app.vo.sku.AddProductRequest;
import com.tyrael.kharazim.product.app.vo.sku.PageProductSkuRequest;
import com.tyrael.kharazim.product.app.vo.sku.ProductSkuDTO;
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
    public DataResponse<ProductSkuDTO> getByCode(@PathVariable("code") String code) {
        return DataResponse.success(productSkuService.getByCode(code));
    }

    @PostMapping("/create")
    @Operation(summary = "创建商品")
    public DataResponse<String> create(@RequestBody @Valid AddProductRequest addRequest) {
        return DataResponse.success(productSkuService.create(addRequest));
    }

    @GetMapping("/page")
    @Operation(summary = "商品分页")
    public PageResponse<ProductSkuDTO> page(@ParameterObject PageProductSkuRequest pageRequest) {
        return productSkuService.page(pageRequest);
    }

}
