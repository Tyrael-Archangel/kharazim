package com.tyrael.kharazim.application.pharmacy.service.impl;

import com.tyrael.kharazim.application.pharmacy.mapper.InventoryMapper;
import com.tyrael.kharazim.application.pharmacy.service.InventoryOccupyService;
import com.tyrael.kharazim.application.prescription.domain.Prescription;
import com.tyrael.kharazim.application.prescription.domain.PrescriptionProduct;
import com.tyrael.kharazim.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;

/**
 * @author Tyrael Archangel
 * @since 2024/7/2
 */
@Service
@RequiredArgsConstructor
public class InventoryOccupyServiceImpl implements InventoryOccupyService {

    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void occupyByPrescription(Prescription prescription) {

        prescription.getProducts()
                .stream()
                .sorted(Comparator.comparing(PrescriptionProduct::getSkuCode))
                .forEach(e -> {
                    int updatedRow = inventoryMapper.increaseOccupy(
                            prescription.getClinicCode(),
                            e.getSkuCode(),
                            e.getQuantity());
                    BusinessException.assertTrue(updatedRow > 0, "商品库存不足");
                });

        // save occupy log
    }

}
