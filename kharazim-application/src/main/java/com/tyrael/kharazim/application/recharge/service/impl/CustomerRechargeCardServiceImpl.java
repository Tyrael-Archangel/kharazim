package com.tyrael.kharazim.application.recharge.service.impl;

import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.customer.domain.Customer;
import com.tyrael.kharazim.application.customer.mapper.CustomerMapper;
import com.tyrael.kharazim.application.recharge.domain.CustomerRechargeCard;
import com.tyrael.kharazim.application.recharge.domain.RechargeCardType;
import com.tyrael.kharazim.application.recharge.mapper.CustomerRechargeCardMapper;
import com.tyrael.kharazim.application.recharge.mapper.RechargeCardTypeMapper;
import com.tyrael.kharazim.application.recharge.service.CustomerRechargeCardService;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeCardPageRequest;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeCardVO;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeRequest;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.application.user.domain.User;
import com.tyrael.kharazim.application.user.mapper.UserMapper;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CustomerRechargeCardVO> page(CustomerRechargeCardPageRequest pageRequest) {
        PageResponse<CustomerRechargeCard> pageData = customerRechargeCardMapper.page(pageRequest);
        return PageResponse.success(
                customerRechargeCards(pageData.getData()),
                pageData.getTotalCount(),
                pageData.getPageSize(),
                pageData.getPageNum());
    }

    private List<CustomerRechargeCardVO> customerRechargeCards(Collection<CustomerRechargeCard> customerRechargeCards) {
        Set<String> customerCodes = Sets.newHashSet();
        Set<String> userCodes = Sets.newHashSet();
        Set<String> cardTypeCodes = Sets.newHashSet();
        for (CustomerRechargeCard rechargeCard : customerRechargeCards) {
            customerCodes.add(rechargeCard.getCustomerCode());
            userCodes.add(rechargeCard.getTraderUserCode());
            cardTypeCodes.add(rechargeCard.getCardTypeCode());
            String chargebackUserCode = rechargeCard.getChargebackUserCode();
            if (StringUtils.isNotBlank(chargebackUserCode)) {
                userCodes.add(chargebackUserCode);
            }
        }

        Map<String, User> userMap = userMapper.mapByCodes(userCodes);
        Map<String, Customer> customerMap = customerMapper.mapByCodes(customerCodes);
        Map<String, RechargeCardType> rechargeCardTypeMap = rechargeCardTypeMapper.mapByCodes(cardTypeCodes);

        return customerRechargeCards.stream()
                .map(e -> {
                    User traderUser = userMap.get(e.getTraderUserCode());
                    String chargebackUserCode = e.getChargebackUserCode();
                    User chargebackUser = null;
                    if (StringUtils.isNotBlank(chargebackUserCode)) {
                        chargebackUser = userMap.get(chargebackUserCode);
                    }
                    Customer customer = customerMap.get(e.getCustomerCode());
                    RechargeCardType rechargeCardType = rechargeCardTypeMap.get(e.getCardTypeCode());
                    return this.customerRechargeCard(e, traderUser, customer, rechargeCardType, chargebackUser);
                })
                .collect(Collectors.toList());
    }

    private CustomerRechargeCardVO customerRechargeCard(CustomerRechargeCard customerRechargeCard,
                                                        User traderUser,
                                                        Customer customer,
                                                        RechargeCardType rechargeCardType,
                                                        User chargebackUser) {
        return CustomerRechargeCardVO.builder()
                .code(customerRechargeCard.getCode())
                .status(customerRechargeCard.getStatus())
                .customerCode(customer.getCode())
                .customerName(customer.getName())
                .cardTypeCode(rechargeCardType.getCode())
                .cardTypeName(rechargeCardType.getName())
                .amount(customerRechargeCard.getTotalAmount())
                .balanceAmount(customerRechargeCard.getBalanceAmount())
                .consumedAmount(customerRechargeCard.getConsumedAmount())
                .consumedOriginalAmount(customerRechargeCard.getConsumedOriginalAmount())
                .traderUserCode(traderUser.getCode())
                .traderUserName(traderUser.getNickName())
                .discountPercent(customerRechargeCard.getDiscountPercent())
                .neverExpire(customerRechargeCard.getNeverExpire())
                .expireDate(customerRechargeCard.getExpireDate())
                .rechargeDate(customerRechargeCard.getRechargeDate())
                .chargebackAmount(customerRechargeCard.getChargebackAmount())
                .chargebackUserCode(chargebackUser == null ? null : chargebackUser.getCode())
                .chargebackUserName(chargebackUser == null ? null : chargebackUser.getNickName())
                .build();
    }

}
