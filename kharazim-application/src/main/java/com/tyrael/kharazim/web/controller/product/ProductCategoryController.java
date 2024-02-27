package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.application.product.service.ProductCategoryService;
import com.tyrael.kharazim.application.product.vo.category.ProductCategoryTreeNodeDTO;
import com.tyrael.kharazim.common.dto.MultiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tyrael Archangel
 * @since 2024/2/27
 */
@RestController
@RequestMapping("/product/category")
@RequiredArgsConstructor
@Tag(name = "商品分类")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping("/tree")
    @Operation(summary = "商品分类树数据")
    public MultiResponse<ProductCategoryTreeNodeDTO> categoryTree() {
        return MultiResponse.success(productCategoryService.tree());
    }

}
