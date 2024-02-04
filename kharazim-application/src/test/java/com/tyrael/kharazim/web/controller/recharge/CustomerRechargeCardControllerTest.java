package com.tyrael.kharazim.web.controller.recharge;

import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeCardPageRequest;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/2/1
 */
class CustomerRechargeCardControllerTest extends BaseControllerTest<CustomerRechargeCardController> {

    CustomerRechargeCardControllerTest() {
        super(CustomerRechargeCardController.class);
    }

    @Test
    void recharge() {
        CustomerRechargeRequest rechargeRequest = new CustomerRechargeRequest();
        rechargeRequest.setCustomerCode("CU0000000002");
        rechargeRequest.setCardTypeCode("RCT000001");
        rechargeRequest.setRechargeDate(LocalDate.now());
        rechargeRequest.setAmount(BigDecimal.valueOf(8000));
        rechargeRequest.setTraderUserCode("U000002");
        rechargeRequest.setRemark("测试: " + LocalDateTime.now());
        super.performWhenCall(mockController.recharge(rechargeRequest));
    }

    @Test
    void rechargeCardPage() {
        CustomerRechargeCardPageRequest pageRequest = new CustomerRechargeCardPageRequest();
        super.performWhenCall(mockController.rechargeCardPage(pageRequest));
    }

}
