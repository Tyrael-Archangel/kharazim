package com.tyrael.kharazim.application.settlement.service.impl;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.prescription.event.PrescriptionPaidEvent;
import com.tyrael.kharazim.application.recharge.service.CustomerRechargeCardService;
import com.tyrael.kharazim.application.settlement.domain.SettlementOrder;
import com.tyrael.kharazim.application.settlement.enums.SettlementOrderStatus;
import com.tyrael.kharazim.application.settlement.mapper.SettlementOrderMapper;
import com.tyrael.kharazim.application.settlement.service.SettlementPayService;
import com.tyrael.kharazim.application.settlement.vo.SettlementPayCommand;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payWithRechargeCard(SettlementPayCommand payCommand, AuthUser currentUser) {

        SettlementOrder settlementOrder = settlementOrderMapper.findByCode(payCommand.getSettlementOrderCode());
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

        // 通知处方已结算
        publisher.publishEvent(new PrescriptionPaidEvent(this, settlementOrder.getSourcePrescriptionCode()));
    }

}
