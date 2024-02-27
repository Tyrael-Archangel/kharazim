package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

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

}