package com.tyrael.kharazim.application.settlement.service;

import com.tyrael.kharazim.application.prescription.domain.Prescription;
import com.tyrael.kharazim.application.settlement.vo.PageSettlementOrderRequest;
import com.tyrael.kharazim.application.settlement.vo.SettlementOrderVO;
import com.tyrael.kharazim.common.dto.PageResponse;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
public interface SettlementOrderService {

    /**
     * 根据处方创建结算单
     *
     * @param prescription {@linkplain Prescription 处方}
     */
    void createFromPrescription(Prescription prescription);

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
