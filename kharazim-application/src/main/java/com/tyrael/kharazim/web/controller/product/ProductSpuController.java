package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.application.product.service.ProductSpuService;
import com.tyrael.kharazim.application.product.vo.spu.AddProductSpuRequest;
import com.tyrael.kharazim.application.product.vo.spu.PageProductSpuRequest;
import com.tyrael.kharazim.application.product.vo.spu.ProductSpuVO;
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
@RequestMapping("/product/spu")
@RequiredArgsConstructor
@Tag(name = "商品")
public class ProductSpuController {

    private final ProductSpuService productSpuService;

    @GetMapping("/{code}")
    @Operation(summary = "SPU信息")
    public DataResponse<ProductSpuVO> getByCode(@PathVariable("code") String code) {
        return DataResponse.ok(productSpuService.getByCode(code));
    }

    @PostMapping("/create")
    @Operation(summary = "创建SPU")
    public DataResponse<String> create(@RequestBody @Valid AddProductSpuRequest addRequest) {
        return DataResponse.ok(productSpuService.create(addRequest));
    }

    @GetMapping("/page")
    @Operation(summary = "SPU分页")
    public PageResponse<ProductSpuVO> page(@ParameterObject PageProductSpuRequest pageRequest) {
        return productSpuService.page(pageRequest);
    }

}
