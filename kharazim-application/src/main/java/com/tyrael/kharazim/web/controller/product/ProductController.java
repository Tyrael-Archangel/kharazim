package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.application.product.service.ProductSpuService;
import com.tyrael.kharazim.application.product.vo.spu.ProductSpuVO;
import com.tyrael.kharazim.common.dto.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2024/3/1
 */
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "商品")
public class ProductController {

    private final ProductSpuService productSpuService;

    @GetMapping("/{code}")
    @Operation(summary = "SPU信息")
    public DataResponse<ProductSpuVO> getByCode(@PathVariable("code") String code) {
        return DataResponse.ok(productSpuService.getByCode(code));
    }

}
