package com.tyrael.kharazim.application.prescription.service.impl;

import com.tyrael.kharazim.application.prescription.mapper.PrescriptionMapper;
import com.tyrael.kharazim.application.prescription.mapper.PrescriptionProductMapper;
import com.tyrael.kharazim.application.prescription.service.PrescriptionService;
import com.tyrael.kharazim.application.prescription.vo.CreatePrescriptionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionMapper prescriptionMapper;
    private final PrescriptionProductMapper prescriptionProductMapper;

    @Override
    public String create(CreatePrescriptionRequest request) {
        // TODO @Tyrael Archangel
        return null;
    }

}
