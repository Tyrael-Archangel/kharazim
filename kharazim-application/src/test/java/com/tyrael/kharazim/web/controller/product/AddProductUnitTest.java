package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.application.product.vo.unit.AddProductUnitRequest;
import com.tyrael.kharazim.common.dto.Pair;
import com.tyrael.kharazim.common.dto.Pairs;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/2/20
 */
public class AddProductUnitTest extends BaseControllerTest<ProductUnitController> {

    public AddProductUnitTest() {
        super(ProductUnitController.class);
    }

    @Test
    void add() {
        Pairs<String, String> units = new Pairs<String, String>()
                .append("克", "g")
                .append("千克", "kg")
                .append("两", "50g")
                .append("吨", "t")
                .append("瓶", "bottle")
                .append("杯", "cup")
                .append("个", null)
                .append("盒", "box")
                .append("箱", "box")
                .append("袋", "bag")
                .append("斤", "500g")
                .append("次", "time")
                .append("片", "slice")
                .append("厘米", "cm")
                .append("双", "pair")
                .append("台", null);
        for (Pair<String, String> unit : units) {
            AddProductUnitRequest addUnitRequest = new AddProductUnitRequest();
            addUnitRequest.setName(unit.left());
            addUnitRequest.setEnglishName(unit.right());
            super.performWhenCall(mockController.add(addUnitRequest));
        }
    }

}