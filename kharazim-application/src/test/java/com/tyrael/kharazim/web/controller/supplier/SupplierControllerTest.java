package com.tyrael.kharazim.web.controller.supplier;

import com.tyrael.kharazim.application.supplier.vo.AddSupplierRequest;
import com.tyrael.kharazim.application.supplier.vo.ListSupplierRequest;
import com.tyrael.kharazim.application.supplier.vo.PageSupplierRequest;
import com.tyrael.kharazim.common.dto.Pair;
import com.tyrael.kharazim.common.dto.Pairs;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
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

    @Test
    void add() {
        Pairs<String, String> suppliers = new Pairs<String, String>()
                .append("魔兽争霸", "Warcraft")
                .append("星际争霸", "StarCraft")
                .append("暗黑破坏神", "Diablo")
                .append("守望先锋", "Overwatch")
                .append("时空枢纽", "Nexus");

        for (Pair<String, String> supplier : suppliers) {
            AddSupplierRequest addSupplierRequest = new AddSupplierRequest();
            addSupplierRequest.setName(supplier.left());
            addSupplierRequest.setRemark(supplier.right());
            super.performWhenCall(mockController.add(addSupplierRequest));
        }
    }

}