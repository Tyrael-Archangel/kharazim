package com.tyrael.kharazim.pharmacy.app.service;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.pharmacy.app.vo.inbound.AddInboundRequest;
import com.tyrael.kharazim.pharmacy.app.vo.inbound.InboundOrderVO;
import com.tyrael.kharazim.pharmacy.app.vo.inbound.PageInboundOrderRequest;
import com.tyrael.kharazim.pharmacy.model.message.CreatePurchaseInboundOrderMessage;
import com.tyrael.kharazim.user.sdk.model.AuthUser;

/**
 * @author Tyrael Archangel
 * @since 2025/4/7
 */
public interface InboundOrderService {

    /**
     * create inbound order from purchase
     *
     * @param message {@linkplain  CreatePurchaseInboundOrderMessage message}
     */
    void createFromPurchase(CreatePurchaseInboundOrderMessage message);

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
