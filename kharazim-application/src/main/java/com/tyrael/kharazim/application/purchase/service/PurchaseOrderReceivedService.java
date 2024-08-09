package com.tyrael.kharazim.application.purchase.service;

import com.tyrael.kharazim.application.purchase.vo.PurchaseOrderReceivedVO;

/**
 * @author Tyrael Archangel
 * @since 2024/8/9
 */
public interface PurchaseOrderReceivedService {

    /**
     * 采购单收货
     *
     * @param receivedVO 采购单收货信息
     */
    void receive(PurchaseOrderReceivedVO receivedVO);

}
