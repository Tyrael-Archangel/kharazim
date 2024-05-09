package com.tyrael.kharazim.web.controller.recharge;

import com.tyrael.kharazim.application.recharge.vo.AddRechargeCardTypeRequest;
import com.tyrael.kharazim.application.recharge.vo.ListRechargeCardTypeRequest;
import com.tyrael.kharazim.application.recharge.vo.ModifyRechargeCardTypeRequest;
import com.tyrael.kharazim.application.recharge.vo.PageRechargeCardTypeRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/26
 */
class RechargeCardTypeControllerTest extends BaseControllerTest<RechargeCardTypeController> {

    RechargeCardTypeControllerTest() {
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
            super.performWhenCall(mockController.create(cardType));
        }
    }

    @Test
    void modify() {
        ModifyRechargeCardTypeRequest modifyRequest = new ModifyRechargeCardTypeRequest();
        modifyRequest.setCode("RCT000001");
        modifyRequest.setName("初级卡");
        modifyRequest.setDiscountPercent(BigDecimal.valueOf(95));
        modifyRequest.setNeverExpire(Boolean.TRUE);
        modifyRequest.setDefaultAmount(BigDecimal.valueOf(5000));
        super.performWhenCall(mockController.modify(modifyRequest, super.mockAdmin()));
    }

    @Test
    @Rollback
    @Transactional(rollbackFor = Exception.class)
    void disableCreateNewCard() {
        String code = "RCT000001";
        super.performWhenCall(mockController.disableCreateNewCard(code, super.mockAdmin()));
    }

    @Test
    void page() {
        PageRechargeCardTypeRequest pageRequest = new PageRechargeCardTypeRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

    @Test
    void list() {
        ListRechargeCardTypeRequest listRequest = new ListRechargeCardTypeRequest();
        super.performWhenCall(mockController.list(listRequest));
    }

}