package com.tyrael.kharazim.application.purchase.service;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.purchase.vo.PurchaseOrderVO;
import com.tyrael.kharazim.application.purchase.vo.request.CreatePurchaseOrderRequest;

/**
 * @author Tyrael Archangel
 * @since 2024/5/31
 */
public interface PurchaseOrderService {

    /**
     * 创建采购单
     *
     * @param request     {@link CreatePurchaseOrderRequest}
     * @param currentUser 创建人
     * @return 返回采购单号
     */
    String create(CreatePurchaseOrderRequest request, AuthUser currentUser);

    /**
     * 采购单详情
     *
     * @param code 采购单号
     * @return 采购单详情
     */
    PurchaseOrderVO detail(String code);

}
