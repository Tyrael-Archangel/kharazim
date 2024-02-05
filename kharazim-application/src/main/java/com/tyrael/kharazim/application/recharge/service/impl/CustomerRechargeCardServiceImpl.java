package com.tyrael.kharazim.application.recharge.service.impl;

import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.customer.domain.Customer;
import com.tyrael.kharazim.application.customer.domain.CustomerWalletTransaction;
import com.tyrael.kharazim.application.customer.enums.TransactionSourceEnum;
import com.tyrael.kharazim.application.customer.enums.TransactionTypeEnum;
import com.tyrael.kharazim.application.customer.mapper.CustomerMapper;
import com.tyrael.kharazim.application.customer.mapper.CustomerWalletTransactionMapper;
import com.tyrael.kharazim.application.recharge.domain.CustomerRechargeCard;
import com.tyrael.kharazim.application.recharge.domain.CustomerRechargeCardLog;
import com.tyrael.kharazim.application.recharge.domain.RechargeCardType;
import com.tyrael.kharazim.application.recharge.enums.CustomerRechargeCardLogType;
import com.tyrael.kharazim.application.recharge.mapper.CustomerRechargeCardLogMapper;
import com.tyrael.kharazim.application.recharge.mapper.CustomerRechargeCardMapper;
import com.tyrael.kharazim.application.recharge.mapper.RechargeCardTypeMapper;
import com.tyrael.kharazim.application.recharge.service.CustomerRechargeCardService;
import com.tyrael.kharazim.application.recharge.vo.*;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.tyrael.kharazim.application.recharge.enums.CustomerRechargeCardStatus.PAID;
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
    private final CustomerRechargeCardLogMapper rechargeCardLogMapper;
    private final UserMapper userMapper;
    private final CustomerMapper customerMapper;
    private final CodeGenerator codeGenerator;
    private final CustomerWalletTransactionMapper customerWalletTransactionMapper;

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
    @Transactional(rollbackFor = Exception.class)
    public void markPaid(String code, AuthUser currentUser) {
        CustomerRechargeCard rechargeCard = customerRechargeCardMapper.findByCode(code);
        DomainNotFoundException.assertFound(rechargeCard, code);

        BusinessException.assertTrue(UNPAID.equals(rechargeCard.getStatus()), "储值单无法标记收款");
        rechargeCard.setStatus(PAID);
        rechargeCard.setUpdate(currentUser.getCode(), currentUser.getNickName());

        int updatedRows = customerRechargeCardMapper.markPaid(rechargeCard);
        BusinessException.assertTrue(updatedRows > 0, "储值单无法标记收款");

        saveRechargeTransaction(rechargeCard, currentUser);
    }

    private void saveRechargeTransaction(CustomerRechargeCard customerRechargeCard, AuthUser currentUser) {

        CustomerRechargeCardLog rechargeCardLog = new CustomerRechargeCardLog();
        rechargeCardLog.setRechargeCardCode(customerRechargeCard.getCode());
        rechargeCardLog.setCustomerCode(customerRechargeCard.getCustomerCode());
        rechargeCardLog.setLogType(CustomerRechargeCardLogType.RECHARGE);
        rechargeCardLog.setSourceBusinessCode(null);
        rechargeCardLog.setCreateTime(LocalDateTime.now());
        rechargeCardLog.setAmount(customerRechargeCard.getTotalAmount());
        rechargeCardLog.setOperator(currentUser.getNickName());
        rechargeCardLog.setOperatorCode(currentUser.getCode());
        rechargeCardLog.setRemark(null);
        rechargeCardLogMapper.insert(rechargeCardLog);

        String transactionCode = codeGenerator.dailyTimeNext(BusinessCodeConstants.CUSTOMER_WALLET_TRANSACTION);
        CustomerWalletTransaction transaction = new CustomerWalletTransaction();
        transaction.setCode(transactionCode);
        transaction.setCustomerCode(customerRechargeCard.getCustomerCode());
        transaction.setType(TransactionTypeEnum.RECHARGE);
        transaction.setSource(TransactionSourceEnum.CUSTOMER_RECHARGE_CARD);
        transaction.setSourceBusinessCode(customerRechargeCard.getCode());
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setAmount(customerRechargeCard.getTotalAmount());
        transaction.setOperator(currentUser.getNickName());
        transaction.setOperatorCode(currentUser.getCode());
        transaction.setRemark(null);
        customerWalletTransactionMapper.insert(transaction);
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

    @Override
    public List<CustomerRechargeCardVO> listCustomerEffective(String customerCode) {
        List<CustomerRechargeCard> rechargeCards = customerRechargeCardMapper.listEffectiveCards(customerCode);
        return customerRechargeCards(rechargeCards);
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

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CustomerRechargeCardLogVO> pageRechargeCardLog(String code,
                                                                       PageCustomerRechargeCardLogRequest pageCommand) {
        PageResponse<CustomerRechargeCardLog> pageResponse = rechargeCardLogMapper.page(code, pageCommand);
        return PageResponse.success(this.rechargeCardLogs(pageResponse.getData()),
                pageResponse.getTotalCount(),
                pageResponse.getPageSize(),
                pageResponse.getPageNum());
    }

    private List<CustomerRechargeCardLogVO> rechargeCardLogs(Collection<CustomerRechargeCardLog> rechargeCardLogs) {
        if (rechargeCardLogs == null || rechargeCardLogs.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> customerCodes = rechargeCardLogs.stream()
                .map(CustomerRechargeCardLog::getCustomerCode)
                .collect(Collectors.toSet());
        Map<String, Customer> customerMap = customerMapper.mapByCodes(customerCodes);
        return rechargeCardLogs.stream()
                .map(e -> {
                    String customerCode = e.getCustomerCode();
                    Customer customer = customerMap.get(customerCode);
                    return CustomerRechargeCardLogVO.builder()
                            .id(e.getId())
                            .rechargeCardCode(e.getRechargeCardCode())
                            .customerCode(customer.getCode())
                            .customerName(customer.getName())
                            .logType(e.getLogType())
                            .sourceBusinessCode(e.getSourceBusinessCode())
                            .createTime(e.getCreateTime())
                            .amount(e.getAmount())
                            .operator(e.getOperator())
                            .operatorCode(e.getOperatorCode())
                            .remark(e.getRemark())
                            .build();
                })
                .collect(Collectors.toList());
    }

}
