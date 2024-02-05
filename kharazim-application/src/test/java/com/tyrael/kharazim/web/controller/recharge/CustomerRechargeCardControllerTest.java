package com.tyrael.kharazim.web.controller.recharge;

import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeCardChargebackRequest;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeCardPageRequest;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeRequest;
import com.tyrael.kharazim.application.recharge.vo.PageCustomerRechargeCardLogRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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
    void markPaid() {
        String code = "CRC20240201000001";
        super.performWhenCall(mockController.markPaid(code, super.mockAdmin()));
    }

    @Test
    @Rollback
    @Transactional(rollbackFor = Exception.class)
    void chargeback() {
        CustomerRechargeCardChargebackRequest chargebackRequest = new CustomerRechargeCardChargebackRequest();
        chargebackRequest.setRechargeCardCode("CRC20240201000001");
        chargebackRequest.setChargebackUserCode("U000002");
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

}
