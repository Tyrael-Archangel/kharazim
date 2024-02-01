package com.tyrael.kharazim.application.recharge.service.impl;

import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.customer.mapper.CustomerMapper;
import com.tyrael.kharazim.application.recharge.domain.CustomerRechargeCard;
import com.tyrael.kharazim.application.recharge.domain.RechargeCardType;
import com.tyrael.kharazim.application.recharge.mapper.CustomerRechargeCardMapper;
import com.tyrael.kharazim.application.recharge.mapper.RechargeCardTypeMapper;
import com.tyrael.kharazim.application.recharge.service.CustomerRechargeCardService;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeRequest;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.application.user.mapper.UserMapper;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.tyrael.kharazim.application.recharge.enums.CustomerRechargeCardStatus.UNPAID;

/**
 * @author Tyrael Archangel
 * @since 2024/2/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerRechargeCardServiceImpl implements CustomerRechargeCardService {

    private final CustomerRechargeCardMapper customerRechargeCardMapper;
    private final RechargeCardTypeMapper rechargeCardTypeMapper;
    private final UserMapper userMapper;
    private final CustomerMapper customerMapper;
    private final CodeGenerator codeGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recharge(CustomerRechargeRequest rechargeRequest) {
        customerMapper.ensureCustomerExist(rechargeRequest.getCustomerCode());
        userMapper.ensureUserExist(rechargeRequest.getTraderUserCode());

        CustomerRechargeCard customerRechargeCard = createCustomerRechargeCard(rechargeRequest);
        customerRechargeCardMapper.insert(customerRechargeCard);
    }

    private CustomerRechargeCard createCustomerRechargeCard(CustomerRechargeRequest rechargeRequest) {

        String cardTypeCode = rechargeRequest.getCardTypeCode();
        RechargeCardType rechargeCardType = rechargeCardTypeMapper.findByCode(cardTypeCode);
        DomainNotFoundException.assertFound(rechargeCardType, cardTypeCode);
        if (rechargeCardType.forbidden()) {
            throw new BusinessException("该储值卡项已禁止发卡");
        }

        String code = codeGenerator.dailyNext(BusinessCodeConstants.CUSTOMER_RECHARGE_CARD);

        CustomerRechargeCard customerRechargeCard = new CustomerRechargeCard();
        customerRechargeCard.setCode(code);
        customerRechargeCard.setStatus(UNPAID);
        customerRechargeCard.setCustomerCode(rechargeRequest.getCustomerCode());
        customerRechargeCard.setCardTypeCode(rechargeCardType.getCode());
        customerRechargeCard.setTotalAmount(rechargeRequest.getAmount());
        customerRechargeCard.setConsumedAmount(BigDecimal.ZERO);
        customerRechargeCard.setConsumedOriginalAmount(BigDecimal.ZERO);
        customerRechargeCard.setTraderUserCode(rechargeRequest.getTraderUserCode());
        customerRechargeCard.setDiscountPercent(rechargeCardType.getDiscountPercent());
        boolean neverExpire = Boolean.TRUE.equals(rechargeCardType.getNeverExpire());
        customerRechargeCard.setNeverExpire(neverExpire);
        LocalDate expireDate = neverExpire ? null : LocalDate.now().plusDays(rechargeCardType.getValidPeriodDays());
        customerRechargeCard.setExpireDate(expireDate);
        customerRechargeCard.setRechargeDate(rechargeRequest.getRechargeDate());
        return customerRechargeCard;
    }

}
