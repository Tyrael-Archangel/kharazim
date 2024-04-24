package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUser;
import com.tyrael.kharazim.application.product.service.ProductCategoryService;
import com.tyrael.kharazim.application.product.vo.category.AddProductCategoryRequest;
import com.tyrael.kharazim.application.product.vo.category.ModifyProductCategoryRequest;
import com.tyrael.kharazim.application.product.vo.category.ProductCategoryTreeNodeDTO;
import com.tyrael.kharazim.application.product.vo.category.ProductCategoryVO;
import com.tyrael.kharazim.common.dto.DataResponse;
import com.tyrael.kharazim.common.dto.MultiResponse;
import com.tyrael.kharazim.common.dto.Response;
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
        return DataResponse.ok(productCategoryService.add(addRequest));
    }

    @PostMapping("/modify")
    @Operation(summary = "修改商品分类")
    public Response modify(@RequestBody @Valid ModifyProductCategoryRequest modifyRequest,
                           @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        productCategoryService.modify(modifyRequest, currentUser);
        return Response.success();
    }

}
