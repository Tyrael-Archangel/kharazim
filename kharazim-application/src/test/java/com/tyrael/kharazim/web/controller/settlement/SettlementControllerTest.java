package com.tyrael.kharazim.web.controller.settlement;

import com.tyrael.kharazim.application.recharge.service.CustomerRechargeCardService;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeCardVO;
import com.tyrael.kharazim.application.settlement.enums.SettlementOrderStatus;
import com.tyrael.kharazim.application.settlement.service.SettlementOrderService;
import com.tyrael.kharazim.application.settlement.vo.PageSettlementOrderRequest;
import com.tyrael.kharazim.application.settlement.vo.SettlementOrderVO;
import com.tyrael.kharazim.application.settlement.vo.SettlementPayCommand;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
class SettlementControllerTest extends BaseControllerTest<SettlementController> {

    @Autowired
    private SettlementOrderService settlementOrderService;

    @Autowired
    private CustomerRechargeCardService customerRechargeCardService;

    SettlementControllerTest() {
        super(SettlementController.class);
    }

    @Test
    void detail() {
        String code = "BO20240403000001";
        super.performWhenCall(mockController.detail(code));
    }

    @Test
    void page() {
        PageSettlementOrderRequest pageRequest = new PageSettlementOrderRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

    @Test
    void payWithRechargeCard() {

        PageSettlementOrderRequest pageRequest = new PageSettlementOrderRequest();
        int pageIndex = 1;

        PageResponse<SettlementOrderVO> pageResponse;
        do {
            pageRequest.setPageIndex(pageIndex++);
            pageResponse = settlementOrderService.page(pageRequest);

            List<SettlementOrderVO> unpaidOrders = pageResponse.getData()
                    .stream()
                    .filter(e -> SettlementOrderStatus.UNPAID.equals(e.getStatus()))
                    .toList();

            for (SettlementOrderVO settlementOrder : unpaidOrders) {
                int randomValue = random.nextInt(100);
                if (randomValue > 10) {
                    this.payWithRechargeCard(settlementOrder);
                }
            }

        } while (pageResponse.isSuccess() && pageResponse.getCurrentPageCount() > 0);
    }

    private void payWithRechargeCard(SettlementOrderVO settlementOrder) {
        List<CustomerRechargeCardVO> customerRechargeCards = customerRechargeCardService
                .listCustomerEffective(settlementOrder.getCustomerCode())
                .stream()
                .sorted(Comparator.comparing(CustomerRechargeCardVO::getBalanceAmount))
                .toList();

        BigDecimal needPayAmount = settlementOrder.getTotalAmount();
        List<SettlementPayCommand.RechargeCardPayDetail> rechargeCardPayDetails = new ArrayList<>();

        for (CustomerRechargeCardVO customerRechargeCard : customerRechargeCards) {
            BigDecimal balanceAmount = customerRechargeCard.getBalanceAmount();
            BigDecimal discountPercent = customerRechargeCard.getDiscountPercent()
                    .divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
            BigDecimal canDeductAmount = balanceAmount.divide(discountPercent, 2, RoundingMode.HALF_UP);
            BigDecimal deductAmount = needPayAmount.compareTo(canDeductAmount) > 0 ? canDeductAmount : needPayAmount;
            BigDecimal useAmount = deductAmount.multiply(discountPercent).setScale(2, RoundingMode.HALF_UP);

            rechargeCardPayDetails.add(SettlementPayCommand.RechargeCardPayDetail.builder()
                    .rechargeCardCode(customerRechargeCard.getCode())
                    .useAmount(useAmount)
                    .deductAmount(deductAmount)
                    .build());

            needPayAmount = needPayAmount.subtract(deductAmount);
            if (needPayAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
        }
        if (needPayAmount.compareTo(BigDecimal.ZERO) <= 0) {
            SettlementPayCommand payCommand = new SettlementPayCommand();
            payCommand.setSettlementOrderCode(settlementOrder.getCode());
            payCommand.setRechargeCardPayDetails(rechargeCardPayDetails);
            super.performWhenCall(mockController.payWithRechargeCard(payCommand, super.mockUser()));
        }
    }

}