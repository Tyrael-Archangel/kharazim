package com.tyrael.kharazim.application.pharmacy.service;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.pharmacy.vo.inboundorder.AddInboundRequest;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrder;

/**
 * @author Tyrael Archangel
 * @since 2024/7/3
 */
public interface InboundOrderService {

    /**
     * 根据采购单创建入库单
     *
     * @param purchaseOrder {@link PurchaseOrder 采购单}
     */
    void createFromPurchaseOrder(PurchaseOrder purchaseOrder);

    /**
     * 商品入库
     *
     * @param addInboundRequest {@link AddInboundRequest}
     * @param operator          操作人
     */
    void inbound(AddInboundRequest addInboundRequest, AuthUser operator);

}
