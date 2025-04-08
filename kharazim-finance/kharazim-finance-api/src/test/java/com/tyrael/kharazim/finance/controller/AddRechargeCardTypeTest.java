package com.tyrael.kharazim.finance.controller;

import com.tyrael.kharazim.authentication.PrincipalHolder;
import com.tyrael.kharazim.finance.DubboReferenceHolder;
import com.tyrael.kharazim.finance.app.vo.recharge.AddRechargeCardTypeRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/26
 */
@SpringBootTest
public class AddRechargeCardTypeTest extends BaseControllerTest<RechargeCardTypeController> {

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

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
            PrincipalHolder.setPrincipal(dubboReferenceHolder.mockUser());
            super.performWhenCall(mockController.create(cardType));
        }
    }

}