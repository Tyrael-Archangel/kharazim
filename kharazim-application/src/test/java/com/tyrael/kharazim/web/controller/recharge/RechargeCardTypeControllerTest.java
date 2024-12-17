package com.tyrael.kharazim.web.controller.recharge;

import com.tyrael.kharazim.application.recharge.vo.ListRechargeCardTypeRequest;
import com.tyrael.kharazim.application.recharge.vo.ModifyRechargeCardTypeRequest;
import com.tyrael.kharazim.application.recharge.vo.PageRechargeCardTypeRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2024/1/26
 */
class RechargeCardTypeControllerTest extends BaseControllerTest<RechargeCardTypeController> {

    RechargeCardTypeControllerTest() {
        super(RechargeCardTypeController.class);
    }

    @Test
    void modify() {
        ModifyRechargeCardTypeRequest modifyRequest = new ModifyRechargeCardTypeRequest();
        modifyRequest.setCode("RCT000001");
        modifyRequest.setName("初级卡");
        modifyRequest.setDiscountPercent(BigDecimal.valueOf(95));
        modifyRequest.setNeverExpire(Boolean.TRUE);
        modifyRequest.setDefaultAmount(BigDecimal.valueOf(5000));
        super.performWhenCall(mockController.modify(modifyRequest, super.mockUser()));
    }

    @Test
    @Rollback
    @Transactional(rollbackFor = Exception.class)
    void disableCreateNewCard() {
        String code = "RCT000001";
        super.performWhenCall(mockController.disableCreateNewCard(code, super.mockUser()));
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