package com.tyrael.kharazim.product.controller;

import com.tyrael.kharazim.authentication.CurrentPrincipal;
import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.base.dto.MultiResponse;
import com.tyrael.kharazim.base.dto.Response;
import com.tyrael.kharazim.product.app.service.ProductCategoryService;
import com.tyrael.kharazim.product.app.vo.category.AddProductCategoryRequest;
import com.tyrael.kharazim.product.app.vo.category.ModifyProductCategoryRequest;
import com.tyrael.kharazim.product.app.vo.category.ProductCategoryTreeNodeDTO;
import com.tyrael.kharazim.product.app.vo.category.ProductCategoryVO;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/all")
    @Operation(summary = "全部商品分类")
    public MultiResponse<ProductCategoryVO> allCategories() {
        return MultiResponse.success(productCategoryService.all());
    }

    @PostMapping("/add")
    @Operation(summary = "新建商品分类")
    public DataResponse<String> add(@RequestBody @Valid AddProductCategoryRequest addRequest) {
        return DataResponse.success(productCategoryService.add(addRequest));
    }

    @PostMapping("/modify")
    @Operation(summary = "修改商品分类")
    public Response modify(@RequestBody @Valid ModifyProductCategoryRequest modifyRequest,
                           @Schema(hidden = true) @CurrentPrincipal AuthUser currentUser) {
        productCategoryService.modify(modifyRequest, currentUser);
        return Response.success();
    }

}
