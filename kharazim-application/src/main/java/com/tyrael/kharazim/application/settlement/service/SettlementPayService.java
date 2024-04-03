package com.tyrael.kharazim.application.settlement.service;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.settlement.vo.SettlementPayCommand;

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
