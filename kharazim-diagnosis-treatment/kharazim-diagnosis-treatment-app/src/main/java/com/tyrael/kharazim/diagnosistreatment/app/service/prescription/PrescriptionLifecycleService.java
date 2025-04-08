package com.tyrael.kharazim.diagnosistreatment.app.service.prescription;

import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.CreatePrescriptionRequest;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
public interface PrescriptionLifecycleService {

    /**
     * 创建处方
     *
     * @param request {@link CreatePrescriptionRequest}
     * @return 处方编码
     */
    String create(CreatePrescriptionRequest request);

    /**
     * 库存预占结果回调
     *
     * @param code     处方编码
     * @param occupied 是否预占成功
     */
    void occupyCallback(String code, boolean occupied);

    /**
     * 处方已支付回调
     *
     * @param code 处方编码
     */
    void paidCallback(String code);

}
