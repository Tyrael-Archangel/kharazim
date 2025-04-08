package com.tyrael.kharazim.finance.app.service;


import com.tyrael.kharazim.finance.app.vo.settlement.SettlementPayCommand;
import com.tyrael.kharazim.user.sdk.model.AuthUser;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
public interface SettlementPayService {

    /**
     * 使用储值单结算
     *
     * @param payCommand  {@link SettlementPayCommand}
     * @param currentUser 操作人
     */
    void payWithRechargeCard(SettlementPayCommand payCommand, AuthUser currentUser);

}
