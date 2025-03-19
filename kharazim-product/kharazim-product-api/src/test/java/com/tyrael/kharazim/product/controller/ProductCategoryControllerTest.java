package com.tyrael.kharazim.product.controller;

import com.tyrael.kharazim.test.mock.BaseControllerTest;
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

    @Test
    void allCategories() {
        super.performWhenCall(mockController.allCategories());
    }

}