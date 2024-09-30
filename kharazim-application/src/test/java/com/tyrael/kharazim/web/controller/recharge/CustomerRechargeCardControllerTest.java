package com.tyrael.kharazim.web.controller.recharge;

import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeCardChargebackRequest;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeCardPageRequest;
import com.tyrael.kharazim.application.recharge.vo.PageCustomerRechargeCardLogRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2024/2/1
 */
class CustomerRechargeCardControllerTest extends BaseControllerTest<CustomerRechargeCardController> {

    CustomerRechargeCardControllerTest() {
        super(CustomerRechargeCardController.class);
    }

    @Test
    @Rollback
    @Transactional(rollbackFor = Exception.class)
    void chargeback() {
        CustomerRechargeCardChargebackRequest chargebackRequest = new CustomerRechargeCardChargebackRequest();
        chargebackRequest.setRechargeCardCode("CRC20240201000001");
        chargebackRequest.setChargebackAmount(BigDecimal.valueOf(100));
        super.performWhenCall(mockController.chargeback(chargebackRequest, super.mockAdmin()));
    }

    @Test
    void rechargeCardPage() {
        CustomerRechargeCardPageRequest pageRequest = new CustomerRechargeCardPageRequest();
        super.performWhenCall(mockController.rechargeCardPage(pageRequest));
    }

    @Test
    void listCustomerEffective() {
        String customerCode = "CU0000000002";
        super.performWhenCall(mockController.listCustomerEffective(customerCode));
    }

    @Test
    void pageRechargeCardLog() {
        String rechargeCardCode = "CRC20240201000001";
        PageCustomerRechargeCardLogRequest pageCommand = new PageCustomerRechargeCardLogRequest();
        super.performWhenCall(mockController.pageRechargeCardLog(rechargeCardCode, pageCommand));
    }

    @Test
    void markRefunded() {
        String code = "CRC20240201000001";
        super.performWhenCall(mockController.markRefunded(code, super.mockAdmin()));
    }

    @Test
    void balanceOverview() {
        String customerCode = "CU0000000002";
        super.performWhenCall(mockController.balanceOverview(customerCode));
    }

    @Test
    void rechargeCardBalance() {
        String customerCode = "CU0000000002";
        super.performWhenCall(mockController.rechargeCardBalance(customerCode));
    }

}
