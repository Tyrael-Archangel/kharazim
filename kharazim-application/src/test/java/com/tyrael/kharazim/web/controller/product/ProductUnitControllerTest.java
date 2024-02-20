package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.application.product.vo.ListProductUnitRequest;
import com.tyrael.kharazim.application.product.vo.PageProductUnitRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/2/20
 */
class ProductUnitControllerTest extends BaseControllerTest<ProductUnitController> {

    ProductUnitControllerTest() {
        super(ProductUnitController.class);
    }

    @Test
    void page() {
        PageProductUnitRequest pageRequest = new PageProductUnitRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

    @Test
    void list() {
        ListProductUnitRequest listRequest = new ListProductUnitRequest();
        super.performWhenCall(mockController.list(listRequest));
    }

}