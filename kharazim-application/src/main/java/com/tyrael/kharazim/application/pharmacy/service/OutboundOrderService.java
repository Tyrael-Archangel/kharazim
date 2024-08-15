package com.tyrael.kharazim.application.pharmacy.service;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.pharmacy.vo.outboundorder.OutboundOrderVO;
import com.tyrael.kharazim.application.pharmacy.vo.outboundorder.OutboundRequest;
import com.tyrael.kharazim.application.pharmacy.vo.outboundorder.PageOutboundOrderRequest;
import com.tyrael.kharazim.application.prescription.domain.Prescription;
import com.tyrael.kharazim.common.dto.PageResponse;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
public interface OutboundOrderService {

    /**
     * 根据处方创建出库单
     *
     * @param prescription {@link Prescription 处方}
     */
    void createFromPrescription(Prescription prescription);

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
     * @param currentUser 操作人
     */
    void outbound(OutboundRequest outboundRequest, AuthUser currentUser);

}
