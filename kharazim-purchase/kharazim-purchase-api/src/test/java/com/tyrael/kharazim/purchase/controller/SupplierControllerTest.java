package com.tyrael.kharazim.purchase.controller;

import com.tyrael.kharazim.purchase.app.vo.supplier.ListSupplierRequest;
import com.tyrael.kharazim.purchase.app.vo.supplier.PageSupplierRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/2/22
 */
class SupplierControllerTest extends BaseControllerTest<SupplierController> {

    SupplierControllerTest() {
        super(SupplierController.class);
    }

    @Test
    void page() {
        PageSupplierRequest pageRequest = new PageSupplierRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

    @Test
    void list() {
        ListSupplierRequest listRequest = new ListSupplierRequest();
        super.performWhenCall(mockController.list(listRequest));
    }

}