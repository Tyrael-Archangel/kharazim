package com.tyrael.kharazim.finance.app.service.impl;

import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.finance.app.domain.SettlementOrder;
import com.tyrael.kharazim.finance.app.enums.SettlementOrderStatus;
import com.tyrael.kharazim.finance.app.mapper.SettlementOrderMapper;
import com.tyrael.kharazim.finance.app.service.CustomerRechargeCardService;
import com.tyrael.kharazim.finance.app.service.SettlementPayService;
import com.tyrael.kharazim.finance.app.vo.settlement.SettlementPayCommand;
import com.tyrael.kharazim.finance.sdk.model.message.SettlementOrderPaidMessage;
import com.tyrael.kharazim.mq.MqProducer;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Service
@RequiredArgsConstructor
public class SettlementPayServiceImpl implements SettlementPayService {

    private final SettlementOrderMapper settlementOrderMapper;
    private final CustomerRechargeCardService customerRechargeCardService;
    private final MqProducer mqProducer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payWithRechargeCard(SettlementPayCommand payCommand, AuthUser currentUser) {

        SettlementOrder settlementOrder = settlementOrderMapper.findByCodeForUpdate(payCommand.getSettlementOrderCode());
        DomainNotFoundException.assertFound(settlementOrder, payCommand.getSettlementOrderCode());
        BusinessException.assertTrue(SettlementOrderStatus.UNPAID.equals(settlementOrder.getStatus()),
                "该结算单已结算");

        List<SettlementPayCommand.RechargeCardPayDetail> payDetails = payCommand.getRechargeCardPayDetails();

        BigDecimal sumDeductAmount = payDetails.stream()
                .map(SettlementPayCommand.RechargeCardPayDetail::getDeductAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BusinessException.assertTrue(settlementOrder.getTotalAmount().compareTo(sumDeductAmount) == 0,
                "结算单金额与抵扣金额不匹配");

        customerRechargeCardService.deduct(settlementOrder.getCustomerCode(), payCommand, currentUser);

        settlementOrder.settlement();
        boolean success = settlementOrderMapper.saveSettlement(settlementOrder);
        BusinessException.assertTrue(success, "结算单结算失败");

        this.publishSettlementOrderPaid(settlementOrder);
    }

    private void publishSettlementOrderPaid(SettlementOrder settlementOrder) {
        SettlementOrderPaidMessage message = new SettlementOrderPaidMessage()
                .setSettlementOrderCode(settlementOrder.getCode())
                .setSourcePrescriptionCode(settlementOrder.getSourcePrescriptionCode())
                .setPaidTime(settlementOrder.getSettlementTime());
        mqProducer.send("SETTLEMENT_ORDER_PAID", message);
    }

}
