package com.tyrael.kharazim.application.purchase.service;

import com.tyrael.kharazim.application.purchase.vo.PurchaseOrderVO;

/**
 * @author Tyrael Archangel
 * @since 2024/5/31
 */
public interface QueryPurchaseOrderService {

    /**
     * 采购单详情
     *
     * @param code 采购单号
     * @return 采购单详情
     */
    PurchaseOrderVO detail(String code);

}
