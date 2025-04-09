package com.tyrael.kharazim.pharmacy.app.service;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.pharmacy.app.vo.outbound.OutboundOrderVO;
import com.tyrael.kharazim.pharmacy.app.vo.outbound.OutboundRequest;
import com.tyrael.kharazim.pharmacy.app.vo.outbound.PageOutboundOrderRequest;
import com.tyrael.kharazim.pharmacy.sdk.model.message.CreatePrescriptionOutboundOrderMessage;
import com.tyrael.kharazim.user.sdk.model.AuthUser;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
public interface OutboundOrderService {

    /**
     * 根据处方创建出库单
     *
     * @param message {@link CreatePrescriptionOutboundOrderMessage 处方}
     */
    void createFromPrescription(CreatePrescriptionOutboundOrderMessage message);

    /**
     * 出库单分页
     *
     * @param pageRequest {@link PageOutboundOrderRequest}
     * @return 出库单分页数据
     */
    PageResponse<OutboundOrderVO> page(PageOutboundOrderRequest pageRequest);

    /**
     * 出库
     *
     * @param outboundRequest {@link OutboundRequest}
     * @param currentUser     操作人
     */
    void outbound(OutboundRequest outboundRequest, AuthUser currentUser);

}
