package com.tyrael.kharazim.basicdata.controller.recharge;

import com.tyrael.kharazim.basicdata.BasicDataApiApplication;
import com.tyrael.kharazim.basicdata.DubboReferenceHolder;
import com.tyrael.kharazim.basicdata.app.dto.recharge.ListRechargeCardTypeRequest;
import com.tyrael.kharazim.basicdata.app.dto.recharge.ModifyRechargeCardTypeRequest;
import com.tyrael.kharazim.basicdata.app.dto.recharge.PageRechargeCardTypeRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2024/1/26
 */
@SpringBootTest(classes = BasicDataApiApplication.class)
class RechargeCardTypeControllerTest extends BaseControllerTest<RechargeCardTypeController> {

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

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
        super.performWhenCall(mockController.modify(modifyRequest, dubboReferenceHolder.mockUser()));
    }

    @Test
    @Rollback
    @Transactional(rollbackFor = Exception.class)
    void disableCreateNewCard() {
        String code = "RCT000001";
        super.performWhenCall(mockController.disableCreateNewCard(code, dubboReferenceHolder.mockUser()));
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