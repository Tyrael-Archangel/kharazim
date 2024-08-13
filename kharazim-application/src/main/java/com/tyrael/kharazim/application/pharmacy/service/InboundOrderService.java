package com.tyrael.kharazim.application.pharmacy.service;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.pharmacy.vo.inboundorder.AddInboundRequest;
import com.tyrael.kharazim.application.pharmacy.vo.inboundorder.InboundOrderVO;
import com.tyrael.kharazim.application.pharmacy.vo.inboundorder.PageInboundOrderRequest;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrder;
import com.tyrael.kharazim.common.dto.PageResponse;

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
     * 入库单分页
     *
     * @param pageRequest {@link PageInboundOrderRequest}
     * @return 入库单分页数据
     */
    PageResponse<InboundOrderVO> page(PageInboundOrderRequest pageRequest);

    /**
     * 商品入库
     *
     * @param addInboundRequest {@link AddInboundRequest}
     * @param operator          操作人
     */
    void inbound(AddInboundRequest addInboundRequest, AuthUser operator);

}
