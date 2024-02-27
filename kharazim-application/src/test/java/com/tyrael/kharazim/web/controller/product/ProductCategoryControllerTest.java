package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.application.product.vo.category.AddProductCategoryRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Tyrael Archangel
 * @since 2024/2/27
 */
class ProductCategoryControllerTest extends BaseControllerTest<ProductCategoryController> {

    ProductCategoryControllerTest() {
        super(ProductCategoryController.class);
    }

    @Test
    void categoryTree() {
        super.performWhenCall(mockController.categoryTree());
    }

    @Test
    @Rollback
    @Transactional(rollbackFor = Exception.class)
    void add() {
        AddProductCategoryRequest addRequest = new AddProductCategoryRequest();
        addRequest.setParentId(null);
        addRequest.setName("中药");
        addRequest.setRemark("--中药--");
        super.performWhenCall(mockController.add(addRequest));
    }

}