package com.tyrael.kharazim.finance.app.service;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.finance.app.vo.recharge.*;
import com.tyrael.kharazim.finance.app.vo.settlement.SettlementPayCommand;
import com.tyrael.kharazim.user.sdk.model.AuthUser;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/2/1
 */
public interface CustomerRechargeCardService {

    /**
     * 充值
     *
     * @param rechargeRequest {@link CustomerRechargeRequest}
     */
    void recharge(CustomerRechargeRequest rechargeRequest);

    /**
     * 更新储值单为已收款
     *
     * @param code        储值单号
     * @param currentUser 操作人
     */
    void markPaid(String code, AuthUser currentUser);

    /**
     * 退卡
     *
     * @param chargebackRequest {@link CustomerRechargeCardChargebackRequest}
     * @param currentUser       操作人
     */
    void chargeback(CustomerRechargeCardChargebackRequest chargebackRequest, AuthUser currentUser);

    /**
     * 抵扣消费
     *
     * @param customerCode 会员编码
     * @param payCommand   {@link SettlementPayCommand}
     * @param currentUser  操作人
     */
    void deduct(String customerCode, SettlementPayCommand payCommand, AuthUser currentUser);

    /**
     * 会员储值单分页
     *
     * @param pageRequest {@link CustomerRechargeCardPageRequest}
     * @return 会员储值单分页数据
     */
    PageResponse<CustomerRechargeCardVO> page(CustomerRechargeCardPageRequest pageRequest);

    /**
     * 查询会员有效的储值单
     *
     * @param customerCode 会员编码
     * @return 会员有效的储值单
     */
    List<CustomerRechargeCardVO> listCustomerEffective(String customerCode);

    /**
     * 储值单分页日志记录
     *
     * @param code        储值单号
     * @param pageCommand 分页条件
     * @return 储值单分页日志记录
     */
    PageResponse<CustomerRechargeCardLogVO> pageRechargeCardLog(String code,
                                                                PageCustomerRechargeCardLogRequest pageCommand);

    /**
     * 更新储值单为已退款
     *
     * @param code        储值单号
     * @param currentUser 操作人
     */
    void markRefunded(String code, AuthUser currentUser);

    /**
     * 会员账户金额总览
     *
     * @param customerCode 会员编码
     * @return 会员账户金额总览
     */
    CustomerBalanceOverviewVO customerBalanceOverview(String customerCode);

    /**
     * 会员储值卡项剩余金额统计
     *
     * @param customerCode 会员编码
     * @return 会员储值卡项剩余金额统计
     */
    List<CustomerRechargeCardTypeBalanceVO> customerRechargeCardTypeBalance(String customerCode);

}
