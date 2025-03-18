package com.tyrael.kharazim.basicdata.controller.supplier;

import com.tyrael.kharazim.authentication.PrincipalHolder;
import com.tyrael.kharazim.base.dto.Pair;
import com.tyrael.kharazim.base.dto.Pairs;
import com.tyrael.kharazim.basicdata.BasicDataApiApplication;
import com.tyrael.kharazim.basicdata.DubboReferenceHolder;
import com.tyrael.kharazim.basicdata.app.dto.supplier.AddSupplierRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Tyrael Archangel
 * @since 2024/2/22
 */
@SpringBootTest(classes = BasicDataApiApplication.class)
public class AddSupplierTest extends BaseControllerTest<SupplierController> {

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

    public AddSupplierTest() {
        super(SupplierController.class);
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
            PrincipalHolder.setPrincipal(dubboReferenceHolder.mockUser());
            super.performWhenCall(mockController.add(addSupplierRequest));
        }
    }

}