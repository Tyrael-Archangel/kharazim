package com.tyrael.kharazim.product.controller;

import com.tyrael.kharazim.product.DubboReferenceHolder;
import com.tyrael.kharazim.product.ProductApiApplication;
import com.tyrael.kharazim.product.app.vo.unit.ListProductUnitRequest;
import com.tyrael.kharazim.product.app.vo.unit.ModifyProductUnitRequest;
import com.tyrael.kharazim.product.app.vo.unit.PageProductUnitRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Tyrael Archangel
 * @since 2024/2/20
 */
@SpringBootTest(classes = ProductApiApplication.class)
class ProductUnitControllerTest extends BaseControllerTest<ProductUnitController> {

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

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

    @Test
    void modify() {
        ModifyProductUnitRequest modifyUnitRequest = new ModifyProductUnitRequest();
        modifyUnitRequest.setCode("UT0001");
        modifyUnitRequest.setEnglishName("gram");
        super.performWhenCall(mockController.modify(modifyUnitRequest, dubboReferenceHolder.mockUser()));
    }

}