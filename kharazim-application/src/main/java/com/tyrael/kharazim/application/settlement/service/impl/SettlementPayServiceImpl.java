package com.tyrael.kharazim.application.settlement.service.impl;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.settlement.service.SettlementPayService;
import com.tyrael.kharazim.application.settlement.vo.SettlementPayCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Service
@RequiredArgsConstructor
public class SettlementPayServiceImpl implements SettlementPayService {

    @Override
    public void payWithRechargeCard(SettlementPayCommand payCommand, AuthUser currentUser) {
        // TODO @Tyrael Archangel
    }

}
