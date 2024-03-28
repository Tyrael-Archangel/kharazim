package com.tyrael.kharazim.application.prescription.converter;

import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.customer.domain.Customer;
import com.tyrael.kharazim.application.customer.mapper.CustomerMapper;
import com.tyrael.kharazim.application.prescription.domain.Prescription;
import com.tyrael.kharazim.application.prescription.domain.PrescriptionProduct;
import com.tyrael.kharazim.application.prescription.vo.PrescriptionVO;
import com.tyrael.kharazim.application.product.service.ProductSkuRepository;
import com.tyrael.kharazim.application.product.vo.sku.ProductSkuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/3/28
 */
@Component
@RequiredArgsConstructor
public class PrescriptionConverter {

    private final ProductSkuRepository productSkuRepository;
    private final CustomerMapper customerMapper;
    private final ClinicMapper clinicMapper;

    /**
     * Prescription、PrescriptionProducts -> PrescriptionVO
     */
    public PrescriptionVO prescriptionVO(Prescription prescription,
                                         Collection<PrescriptionProduct> prescriptionProducts) {
        if (prescription == null) {
            return null;
        }
        return prescriptionVOs(Collections.singleton(prescription), prescriptionProducts)
                .iterator().next();
    }

    /**
     * Prescriptions、PrescriptionProducts -> PrescriptionVOs
     */
    public List<PrescriptionVO> prescriptionVOs(Collection<Prescription> prescriptions,
                                                Collection<PrescriptionProduct> prescriptionProducts) {
        if (prescriptions == null || prescriptions.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> customerCodes = Sets.newHashSet();
        Set<String> clinicCodes = Sets.newHashSet();
        for (Prescription prescription : prescriptions) {
            customerCodes.add(prescription.getCustomerCode());
            clinicCodes.add(prescription.getClinicCode());
        }
        Map<String, Customer> customerMap = customerMapper.mapByCodes(customerCodes);
        Map<String, Clinic> clinicMap = clinicMapper.mapByCodes(clinicCodes);

        Map<String, List<PrescriptionProduct>> productGroups;
        Map<String, ProductSkuVO> skuMap;
        if (prescriptionProducts == null || prescriptionProducts.isEmpty()) {
            productGroups = Collections.emptyMap();
            skuMap = Collections.emptyMap();
        } else {
            productGroups = prescriptionProducts.stream()
                    .collect(Collectors.groupingBy(PrescriptionProduct::getPrescriptionCode));
            Set<String> skuCodes = prescriptionProducts.stream()
                    .map(PrescriptionProduct::getSkuCode)
                    .collect(Collectors.toSet());
            skuMap = productSkuRepository.mapByCodes(skuCodes);
        }

        return prescriptions.stream()
                .map(e -> this.prescriptionVO(e,
                        customerMap.get(e.getCustomerCode()),
                        clinicMap.get(e.getClinicCode()),
                        productGroups.getOrDefault(e.getCode(), Collections.emptyList()),
                        skuMap))
                .collect(Collectors.toList());
    }

    private PrescriptionVO prescriptionVO(Prescription prescription,
                                          Customer customer,
                                          Clinic clinic,
                                          List<PrescriptionProduct> prescriptionProducts,
                                          Map<String, ProductSkuVO> skuMap) {
        PrescriptionVO prescriptionVO = new PrescriptionVO();
        prescriptionVO.setCode(prescription.getCode());
        prescriptionVO.setCustomerCode(customer.getCode());
        prescriptionVO.setCustomerName(customer.getName());
        prescriptionVO.setClinicCode(clinic.getCode());
        prescriptionVO.setClinicName(clinic.getName());
        prescriptionVO.setTotalAmount(prescription.getTotalAmount());

        List<PrescriptionVO.Product> products = prescriptionProducts.stream()
                .map(e -> this.prescriptionProductVO(e, skuMap.get(e.getSkuCode())))
                .collect(Collectors.toList());

        prescriptionVO.setProducts(products);
        prescriptionVO.setRemark(prescription.getRemark());
        prescriptionVO.setCreateTime(prescription.getCreateTime());
        prescriptionVO.setCreator(prescription.getCreator());
        prescriptionVO.setCreatorCode(prescription.getCreatorCode());
        return prescriptionVO;
    }

    private PrescriptionVO.Product prescriptionProductVO(PrescriptionProduct prescriptionProduct,
                                                         ProductSkuVO skuInfo) {
        PrescriptionVO.Product product = new PrescriptionVO.Product();
        product.setSkuCode(prescriptionProduct.getSkuCode());
        product.setSkuName(skuInfo.getName());
        product.setCategoryCode(skuInfo.getCategoryCode());
        product.setCategoryName(skuInfo.getCategoryName());
        product.setSupplierCode(skuInfo.getSupplierCode());
        product.setSupplierName(skuInfo.getSupplierName());
        product.setUnitCode(skuInfo.getUnitCode());
        product.setUnitName(skuInfo.getUnitName());
        product.setDescription(skuInfo.getDescription());
        product.setQuantity(prescriptionProduct.getQuantity());
        product.setPrice(prescriptionProduct.getPrice());
        product.setAmount(prescriptionProduct.getAmount());
        return product;
    }

}
