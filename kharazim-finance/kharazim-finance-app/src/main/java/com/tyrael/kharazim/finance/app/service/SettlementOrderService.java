package com.tyrael.kharazim.finance.app.service;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.finance.app.vo.settlement.PageSettlementOrderRequest;
import com.tyrael.kharazim.finance.app.vo.settlement.SettlementOrderVO;
import com.tyrael.kharazim.finance.sdk.model.message.CreatePrescriptionSettlementMessage;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
public interface SettlementOrderService {

    /**
     * 根据处方创建结算单
     *
     * @param message {@link CreatePrescriptionSettlementMessage}
     */
    void createFromPrescription(CreatePrescriptionSettlementMessage message);

    /**
     * find by code
     *
     * @param code 结算单编码
     * @return SettlementOrderVO
     */
    SettlementOrderVO findByCode(String code);

    /**
     * 结算单分页
     *
     * @param pageRequest {@link PageSettlementOrderRequest}
     * @return 结算单分页
     */
    PageResponse<SettlementOrderVO> page(PageSettlementOrderRequest pageRequest);

}
