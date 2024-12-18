package com.tyrael.kharazim.web.controller.recharge;

import com.tyrael.kharazim.application.recharge.vo.AddRechargeCardTypeRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/26
 */
public class AddRechargeCardTypeTest extends BaseControllerTest<RechargeCardTypeController> {

    public AddRechargeCardTypeTest() {
        super(RechargeCardTypeController.class);
    }

    @Test
    void create() {
        List<AddRechargeCardTypeRequest> cardTypes = List.of(
                AddRechargeCardTypeRequest.create("初级卡", 95, false, 180, 5000),
                AddRechargeCardTypeRequest.create("中级卡", 90, false, 360, 10000),
                AddRechargeCardTypeRequest.create("高级卡", 85, false, 720, 50000),
                AddRechargeCardTypeRequest.create("贵宾卡", 80, true, null, 100000),
                AddRechargeCardTypeRequest.create("至尊卡", 70, true, null, 200000)
        );
        for (AddRechargeCardTypeRequest cardType : cardTypes) {
            super.mockRandomUser();
            super.performWhenCall(mockController.create(cardType));
        }
    }

}