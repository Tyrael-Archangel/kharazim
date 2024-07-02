package com.tyrael.kharazim.application.pharmacy.service;

import com.tyrael.kharazim.application.prescription.domain.Prescription;

/**
 * @author Tyrael Archangel
 * @since 2024/7/2
 */
public interface InventoryOccupyService {

    /**
     * 根据处方预占库存
     *
     * @param prescription {@linkplain Prescription 处方}
     */
    void occupyByPrescription(Prescription prescription);

}
