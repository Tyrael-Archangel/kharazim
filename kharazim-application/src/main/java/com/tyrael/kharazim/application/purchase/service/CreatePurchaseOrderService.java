package com.tyrael.kharazim.application.purchase.service;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.purchase.vo.request.CreatePurchaseOrderRequest;

/**
 * @author Tyrael Archangel
 * @since 2024/5/31
 */
public interface CreatePurchaseOrderService {

    /**
     * 创建采购单
     *
     * @param request     {@link CreatePurchaseOrderRequest}
     * @param currentUser 创建人
     * @return 返回采购单号
     */
    String create(CreatePurchaseOrderRequest request, AuthUser currentUser);

}
