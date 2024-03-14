package com.tyrael.kharazim.application.prescription.service;

import com.tyrael.kharazim.application.prescription.vo.CreatePrescriptionRequest;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
public interface PrescriptionService {

    /**
     * 创建处方
     *
     * @param request {@link CreatePrescriptionRequest}
     * @return 处方编码
     */
    String create(CreatePrescriptionRequest request);

}
