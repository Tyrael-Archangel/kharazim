package com.tyrael.kharazim.application.pharmacy.service.impl;

import com.tyrael.kharazim.application.pharmacy.service.InboundOrderService;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Tyrael Archangel
 * @since 2024/7/3
 */
@Service
@RequiredArgsConstructor
public class InboundOrderServiceImpl implements InboundOrderService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFromPurchaseOrder(PurchaseOrder purchaseOrder) {
        // TODO @Tyrael Archangel
    }

}
