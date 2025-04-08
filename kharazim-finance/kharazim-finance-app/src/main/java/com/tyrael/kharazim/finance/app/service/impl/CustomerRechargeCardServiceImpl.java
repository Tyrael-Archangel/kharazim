package com.tyrael.kharazim.finance.app.service.impl;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.basicdata.sdk.model.CustomerVO;
import com.tyrael.kharazim.basicdata.sdk.service.CustomerServiceApi;
import com.tyrael.kharazim.finance.app.constant.FinanceBusinessIdConstants;
import com.tyrael.kharazim.finance.app.domain.CustomerRechargeCard;
import com.tyrael.kharazim.finance.app.domain.CustomerRechargeCardLog;
import com.tyrael.kharazim.finance.app.domain.CustomerWalletTransaction;
import com.tyrael.kharazim.finance.app.domain.RechargeCardType;
import com.tyrael.kharazim.finance.app.enums.CustomerRechargeCardLogType;
import com.tyrael.kharazim.finance.app.enums.CustomerRechargeCardStatus;
import com.tyrael.kharazim.finance.app.enums.TransactionSourceEnum;
import com.tyrael.kharazim.finance.app.enums.TransactionTypeEnum;
import com.tyrael.kharazim.finance.app.mapper.CustomerRechargeCardLogMapper;
import com.tyrael.kharazim.finance.app.mapper.CustomerRechargeCardMapper;
import com.tyrael.kharazim.finance.app.mapper.CustomerWalletTransactionMapper;
import com.tyrael.kharazim.finance.app.mapper.RechargeCardTypeMapper;
import com.tyrael.kharazim.finance.app.service.CustomerRechargeCardService;
import com.tyrael.kharazim.finance.app.vo.recharge.*;
import com.tyrael.kharazim.finance.app.vo.settlement.SettlementPayCommand;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import com.tyrael.kharazim.user.sdk.model.UserSimpleVO;
import com.tyrael.kharazim.user.sdk.service.UserServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final CustomerWalletTransactionMapper customerWalletTransactionMapper;
    private final IdGenerator idGenerator;

    @DubboReference
    private UserServiceApi userServiceApi;
    @DubboReference
    private CustomerServiceApi customerServiceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recharge(CustomerRechargeRequest rechargeRequest) {
        userServiceApi.ensureUserExist(rechargeRequest.getTraderUserCode());

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

        String code = idGenerator.dailyNext(FinanceBusinessIdConstants.CUSTOMER_RECHARGE_CARD);

        CustomerRechargeCard customerRechargeCard = new CustomerRechargeCard();
        customerRechargeCard.setCode(code);
        customerRechargeCard.setStatus(CustomerRechargeCardStatus.UNPAID);
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

        BusinessException.assertTrue(CustomerRechargeCardStatus.UNPAID.equals(rechargeCard.getStatus()), "储值单无法标记收款");
        rechargeCard.setStatus(CustomerRechargeCardStatus.PAID);
        rechargeCard.setUpdateUser(currentUser.getCode(), currentUser.getNickName());

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

        String transactionCode = idGenerator.dailyTimeNext(FinanceBusinessIdConstants.CUSTOMER_WALLET_TRANSACTION);
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
    @Transactional(rollbackFor = Exception.class)
    public void chargeback(CustomerRechargeCardChargebackRequest chargebackRequest, AuthUser currentUser) {
        String rechargeCardCode = chargebackRequest.getRechargeCardCode();
        CustomerRechargeCard rechargeCard = customerRechargeCardMapper.findByCode(rechargeCardCode);
        DomainNotFoundException.assertFound(rechargeCard, "储值单号: " + rechargeCardCode);

        BusinessException.assertTrue(rechargeCard.effective(), "储值单" + rechargeCardCode + "无法退卡");
        BigDecimal chargebackAmount = chargebackRequest.getChargebackAmount();
        BusinessException.assertTrue(
                rechargeCard.getBalanceAmount().compareTo(chargebackAmount) >= 0,
                "退卡金额超出剩余金额");

        rechargeCard.setStatus(CustomerRechargeCardStatus.WAIT_REFUND);
        rechargeCard.setChargebackAmount(chargebackAmount);
        rechargeCard.setChargebackUserCode(currentUser.getCode());
        rechargeCard.setUpdateUser(currentUser.getCode(), currentUser.getNickName());

        int updatedRows = customerRechargeCardMapper.chargeback(rechargeCard);
        BusinessException.assertTrue(updatedRows > 0, "储值单" + rechargeCardCode + "退卡失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deduct(String customerCode, SettlementPayCommand payCommand, AuthUser currentUser) {

        List<SettlementPayCommand.RechargeCardPayDetail> payDetails = payCommand.getRechargeCardPayDetails();
        for (SettlementPayCommand.RechargeCardPayDetail payDetail : payDetails) {
            CustomerRechargeCard rechargeCard = customerRechargeCardMapper.findByCode(payDetail.getRechargeCardCode());
            DomainNotFoundException.assertFound(rechargeCard, payDetail.getRechargeCardCode());

            BusinessException.assertTrue(customerCode.equals(rechargeCard.getCustomerCode()),
                    "储值单[" + rechargeCard.getCode() + "]无法用于该结算单");

            // 记录原始的consumedAmount，用作悲观锁
            BigDecimal originalConsumedAmount = rechargeCard.getConsumedAmount();
            rechargeCard.consume(payDetail.getUseAmount(), payDetail.getDeductAmount());
            rechargeCard.setUpdateUser(currentUser.getCode(), currentUser.getNickName());

            boolean success = customerRechargeCardMapper.consume(originalConsumedAmount, rechargeCard);
            BusinessException.assertTrue(success, "储值单[" + rechargeCard.getCode() + "]抵扣失败");

            saveDeductTransaction(payCommand, rechargeCard, payDetail, currentUser);
        }
    }

    private void saveDeductTransaction(SettlementPayCommand payCommand,
                                       CustomerRechargeCard customerRechargeCard,
                                       SettlementPayCommand.RechargeCardPayDetail payDetail,
                                       AuthUser currentUser) {

        CustomerRechargeCardLog rechargeCardLog = new CustomerRechargeCardLog();
        rechargeCardLog.setRechargeCardCode(customerRechargeCard.getCode());
        rechargeCardLog.setCustomerCode(customerRechargeCard.getCustomerCode());
        rechargeCardLog.setLogType(CustomerRechargeCardLogType.CONSUME);
        rechargeCardLog.setSourceBusinessCode(payCommand.getSettlementOrderCode());
        rechargeCardLog.setCreateTime(LocalDateTime.now());
        rechargeCardLog.setAmount(payDetail.getUseAmount());
        rechargeCardLog.setOperator(currentUser.getNickName());
        rechargeCardLog.setOperatorCode(currentUser.getCode());
        rechargeCardLog.setRemark(null);
        rechargeCardLogMapper.insert(rechargeCardLog);

        String transactionCode = idGenerator.dailyTimeNext(FinanceBusinessIdConstants.CUSTOMER_WALLET_TRANSACTION);
        CustomerWalletTransaction transaction = new CustomerWalletTransaction();
        transaction.setCode(transactionCode);
        transaction.setCustomerCode(customerRechargeCard.getCustomerCode());
        transaction.setType(TransactionTypeEnum.CONSUME);
        transaction.setSource(TransactionSourceEnum.SETTLEMENT_ORDER);
        transaction.setSourceBusinessCode(payCommand.getSettlementOrderCode());
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setAmount(payDetail.getUseAmount());
        transaction.setOperator(currentUser.getNickName());
        transaction.setOperatorCode(currentUser.getCode());
        transaction.setRemark(null);
        customerWalletTransactionMapper.insert(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CustomerRechargeCardVO> page(CustomerRechargeCardPageRequest pageRequest) {
        PageResponse<CustomerRechargeCard> pageData = customerRechargeCardMapper.page(pageRequest);
        return PageResponse.success(customerRechargeCards(pageData.getData()), pageData.getTotalCount());
    }

    @Override
    public List<CustomerRechargeCardVO> listCustomerEffective(String customerCode) {
        List<CustomerRechargeCard> rechargeCards = customerRechargeCardMapper.listEffectiveCards(customerCode);
        return customerRechargeCards(rechargeCards);
    }

    private List<CustomerRechargeCardVO> customerRechargeCards(Collection<CustomerRechargeCard> customerRechargeCards) {
        Set<String> customerCodes = new HashSet<>();
        Set<String> userCodes = new HashSet<>();
        Set<String> cardTypeCodes = new HashSet<>();
        for (CustomerRechargeCard rechargeCard : customerRechargeCards) {
            customerCodes.add(rechargeCard.getCustomerCode());
            userCodes.add(rechargeCard.getTraderUserCode());
            cardTypeCodes.add(rechargeCard.getCardTypeCode());
            String chargebackUserCode = rechargeCard.getChargebackUserCode();
            if (StringUtils.isNotBlank(chargebackUserCode)) {
                userCodes.add(chargebackUserCode);
            }
        }

        Map<String, UserSimpleVO> userMap = userServiceApi.mapByCodes(userCodes);
        Map<String, CustomerVO> customerMap = customerServiceApi.mapByCodes(customerCodes);
        Map<String, RechargeCardType> rechargeCardTypeMap = rechargeCardTypeMapper.mapByCodes(cardTypeCodes);

        return customerRechargeCards.stream()
                .map(e -> {
                    UserSimpleVO traderUser = userMap.get(e.getTraderUserCode());
                    String chargebackUserCode = e.getChargebackUserCode();
                    UserSimpleVO chargebackUser = null;
                    if (StringUtils.isNotBlank(chargebackUserCode)) {
                        chargebackUser = userMap.get(chargebackUserCode);
                    }
                    CustomerVO customer = customerMap.get(e.getCustomerCode());
                    RechargeCardType rechargeCardType = rechargeCardTypeMap.get(e.getCardTypeCode());
                    return this.customerRechargeCard(e, traderUser, customer, rechargeCardType, chargebackUser);
                })
                .collect(Collectors.toList());
    }

    private CustomerRechargeCardVO customerRechargeCard(CustomerRechargeCard customerRechargeCard,
                                                        UserSimpleVO traderUser,
                                                        CustomerVO customer,
                                                        RechargeCardType rechargeCardType,
                                                        UserSimpleVO chargebackUser) {
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
        return PageResponse.success(this.rechargeCardLogs(pageResponse.getData()), pageResponse.getTotalCount());
    }

    private List<CustomerRechargeCardLogVO> rechargeCardLogs(Collection<CustomerRechargeCardLog> rechargeCardLogs) {
        if (rechargeCardLogs == null || rechargeCardLogs.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> customerCodes = rechargeCardLogs.stream()
                .map(CustomerRechargeCardLog::getCustomerCode)
                .collect(Collectors.toSet());
        Map<String, CustomerVO> customerMap = customerServiceApi.mapByCodes(customerCodes);
        return rechargeCardLogs.stream()
                .map(e -> {
                    String customerCode = e.getCustomerCode();
                    CustomerVO customer = customerMap.get(customerCode);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markRefunded(String code, AuthUser currentUser) {
        CustomerRechargeCard rechargeCard = customerRechargeCardMapper.findByCode(code);
        DomainNotFoundException.assertFound(rechargeCard, code);

        BusinessException.assertTrue(CustomerRechargeCardStatus.WAIT_REFUND.equals(rechargeCard.getStatus()), "储值单无法标记退款");
        rechargeCard.setStatus(CustomerRechargeCardStatus.REFUNDED);
        rechargeCard.setUpdateUser(currentUser.getCode(), currentUser.getNickName());

        int updatedRows = customerRechargeCardMapper.markRefunded(rechargeCard);
        BusinessException.assertTrue(updatedRows > 0, "储值单无法标记退款");

        saveRefundTransaction(rechargeCard, currentUser);
    }

    private void saveRefundTransaction(CustomerRechargeCard customerRechargeCard, AuthUser currentUser) {

        CustomerRechargeCardLog rechargeCardLog = new CustomerRechargeCardLog();
        rechargeCardLog.setRechargeCardCode(customerRechargeCard.getCode());
        rechargeCardLog.setCustomerCode(customerRechargeCard.getCustomerCode());
        rechargeCardLog.setLogType(CustomerRechargeCardLogType.REFUND);
        rechargeCardLog.setSourceBusinessCode(null);
        rechargeCardLog.setCreateTime(LocalDateTime.now());
        rechargeCardLog.setAmount(customerRechargeCard.getChargebackAmount());
        rechargeCardLog.setOperator(currentUser.getNickName());
        rechargeCardLog.setOperatorCode(currentUser.getCode());
        rechargeCardLog.setRemark(null);
        rechargeCardLogMapper.insert(rechargeCardLog);

        String transactionCode = idGenerator.dailyTimeNext(FinanceBusinessIdConstants.CUSTOMER_WALLET_TRANSACTION);
        CustomerWalletTransaction transaction = new CustomerWalletTransaction();
        transaction.setCode(transactionCode);
        transaction.setCustomerCode(customerRechargeCard.getCustomerCode());
        transaction.setType(TransactionTypeEnum.REFUND);
        transaction.setSource(TransactionSourceEnum.CUSTOMER_RECHARGE_CARD);
        transaction.setSourceBusinessCode(customerRechargeCard.getCode());
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setAmount(customerRechargeCard.getChargebackAmount());
        transaction.setOperator(currentUser.getNickName());
        transaction.setOperatorCode(currentUser.getCode());
        transaction.setRemark(null);
        customerWalletTransactionMapper.insert(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerBalanceOverviewVO customerBalanceOverview(String customerCode) {
        CustomerVO customer = customerServiceApi.exactlyFindByCode(customerCode);

        List<CustomerRechargeCard> effectiveCards = customerRechargeCardMapper.listEffectiveCards(customerCode);
        BigDecimal totalBalanceAmount = effectiveCards.stream()
                .map(CustomerRechargeCard::getBalanceAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<LocalDate, BigDecimal> expireDateAmounts = effectiveCards.stream()
                .filter(e -> !e.neverExpire())
                .collect(Collectors.groupingBy(
                        CustomerRechargeCard::getExpireDate,
                        Collectors.reducing(BigDecimal.ZERO, CustomerRechargeCard::getBalanceAmount, BigDecimal::add)));
        Map.Entry<LocalDate, BigDecimal> latestExpireDateAndAmount = expireDateAmounts.entrySet()
                .stream()
                .min(Map.Entry.comparingByKey())
                .orElse(null);

        BigDecimal accumulatedRechargeAmount = customerAccumulatedRechargeAmount(customerCode);
        BigDecimal accumulatedConsumedAmount = customerAccumulatedConsumedAmount(customerCode);

        return CustomerBalanceOverviewVO.builder()
                .customerCode(customer.getCode())
                .customerName(customer.getName())
                .totalBalanceAmount(totalBalanceAmount)
                .accumulatedRechargeAmount(accumulatedRechargeAmount)
                .accumulatedConsumedAmount(accumulatedConsumedAmount)
                .latestExpireAmount(latestExpireDateAndAmount == null ? BigDecimal.ZERO : latestExpireDateAndAmount.getValue())
                .expireDate(latestExpireDateAndAmount == null ? null : latestExpireDateAndAmount.getKey())
                .build();
    }

    private BigDecimal customerAccumulatedRechargeAmount(String customerCode) {
        List<CustomerWalletTransaction> customerWalletTransactions = customerWalletTransactionMapper.listByType(
                customerCode, TransactionTypeEnum.RECHARGE);
        return customerWalletTransactions.stream()
                .map(CustomerWalletTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal customerAccumulatedConsumedAmount(String customerCode) {
        List<CustomerWalletTransaction> customerWalletTransactions = customerWalletTransactionMapper.listByType(
                customerCode, TransactionTypeEnum.CONSUME);
        return customerWalletTransactions.stream()
                .map(CustomerWalletTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerRechargeCardTypeBalanceVO> customerRechargeCardTypeBalance(String customerCode) {
        CustomerVO customer = customerServiceApi.exactlyFindByCode(customerCode);

        List<CustomerRechargeCard> effectiveCards = customerRechargeCardMapper.listEffectiveCards(customerCode);
        Map<String, BigDecimal> cardTypeToBalanceMap = effectiveCards.stream()
                .collect(Collectors.groupingBy(CustomerRechargeCard::getCardTypeCode,
                        Collectors.reducing(BigDecimal.ZERO, CustomerRechargeCard::getBalanceAmount, BigDecimal::add)));

        List<RechargeCardType> rechargeCardTypes = rechargeCardTypeMapper.listAll();
        return rechargeCardTypes.stream()
                .map(rechargeCardType -> CustomerRechargeCardTypeBalanceVO.builder()
                        .customerCode(customerCode)
                        .customerName(customer.getName())
                        .balanceAmount(cardTypeToBalanceMap.getOrDefault(rechargeCardType.getCode(), BigDecimal.ZERO))
                        .cardTypeCode(rechargeCardType.getCode())
                        .cardTypeName(rechargeCardType.getName())
                        .build()
                )
                .collect(Collectors.toList());
    }

}
