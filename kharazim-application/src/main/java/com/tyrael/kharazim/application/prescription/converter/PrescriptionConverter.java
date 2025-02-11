package com.tyrael.kharazim.application.prescription.converter;

import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.customer.domain.Customer;
import com.tyrael.kharazim.application.customer.mapper.CustomerMapper;
import com.tyrael.kharazim.application.prescription.domain.Prescription;
import com.tyrael.kharazim.application.prescription.domain.PrescriptionProduct;
import com.tyrael.kharazim.application.prescription.vo.PrescriptionExportVO;
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
        ConverterHelper converterHelper = new ConverterHelper(prescriptions, prescriptionProducts);

        return prescriptions.stream()
                .map(prescription -> {

                    PrescriptionVO prescriptionVO = new PrescriptionVO();
                    prescriptionVO.setCode(prescription.getCode());

                    Customer customer = converterHelper.getCustomer(prescription.getCustomerCode());
                    prescriptionVO.setCustomerCode(customer.getCode());
                    prescriptionVO.setCustomerName(customer.getName());

                    Clinic clinic = converterHelper.getClinic(prescription.getClinicCode());
                    prescriptionVO.setClinicCode(clinic.getCode());
                    prescriptionVO.setClinicName(clinic.getName());

                    prescriptionVO.setTotalAmount(prescription.getTotalAmount());

                    List<PrescriptionVO.Product> products = converterHelper.getPrescriptionProducts(prescription.getCode())
                            .stream()
                            .map(e -> this.prescriptionProductVO(e, converterHelper.getSku(e.getSkuCode())))
                            .collect(Collectors.toList());
                    prescriptionVO.setProducts(products);

                    prescriptionVO.setRemark(prescription.getRemark());
                    prescriptionVO.setCreateTime(prescription.getCreateTime());
                    prescriptionVO.setCreator(prescription.getCreator());
                    prescriptionVO.setCreatorCode(prescription.getCreatorCode());
                    return prescriptionVO;
                })
                .collect(Collectors.toList());
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
        product.setDefaultImageUrl(skuInfo.getDefaultImageUrl());
        product.setDescription(skuInfo.getDescription());
        product.setQuantity(prescriptionProduct.getQuantity());
        product.setPrice(prescriptionProduct.getPrice());
        product.setAmount(prescriptionProduct.getAmount());
        return product;
    }

    /**
     * Prescription、PrescriptionProducts -> PrescriptionExportVOs
     */
    public List<PrescriptionExportVO> prescriptionExportVOs(Collection<Prescription> prescriptions,
                                                            List<PrescriptionProduct> prescriptionProducts) {
        if (prescriptions == null || prescriptions.isEmpty()) {
            return new ArrayList<>();
        }

        ConverterHelper converterHelper = new ConverterHelper(prescriptions, prescriptionProducts);

        List<PrescriptionExportVO> exports = new ArrayList<>();
        for (Prescription prescription : prescriptions) {

            Customer customer = converterHelper.getCustomer(prescription.getCustomerCode());
            Clinic clinic = converterHelper.getClinic(prescription.getClinicCode());
            List<PrescriptionProduct> products = converterHelper.getPrescriptionProducts(prescription.getCode());

            if (products == null || products.isEmpty()) {
                PrescriptionExportVO export = PrescriptionExportVO.builder()
                        .code(prescription.getCode())
                        .customerCode(customer.getCode())
                        .customerName(customer.getName())
                        .clinicName(clinic.getName())
                        .totalAmount(prescription.getTotalAmount())
                        .remark(prescription.getRemark())
                        .createTime(prescription.getCreateTime())
                        .creator(prescription.getCreator())
                        .build();
                exports.add(export);
            } else {
                for (PrescriptionProduct product : products) {
                    ProductSkuVO skuInfo = converterHelper.getSku(product.getSkuCode());
                    PrescriptionExportVO export = PrescriptionExportVO.builder()
                            .code(prescription.getCode())
                            .customerCode(customer.getCode())
                            .customerName(customer.getName())
                            .clinicName(clinic.getName())
                            .totalAmount(prescription.getTotalAmount())
                            .remark(prescription.getRemark())
                            .createTime(prescription.getCreateTime())
                            .creator(prescription.getCreator())
                            .skuCode(skuInfo.getCode())
                            .skuName(skuInfo.getName())
                            .categoryName(skuInfo.getCategoryName())
                            .supplierName(skuInfo.getSupplierName())
                            .unitName(skuInfo.getUnitName())
                            .quantity(product.getQuantity())
                            .price(product.getPrice())
                            .amount(product.getAmount())
                            .build();
                    exports.add(export);
                }
            }
        }
        return exports;
    }

    private class ConverterHelper {
        private final Collection<Prescription> prescriptions;
        private final Collection<PrescriptionProduct> prescriptionProducts;

        private Map<String, Customer> customerMap;
        private Map<String, Clinic> clinicMap;
        private Map<String, List<PrescriptionProduct>> productGroups;
        private Map<String, ProductSkuVO> skuMap;

        public ConverterHelper(Collection<Prescription> prescriptions,
                               Collection<PrescriptionProduct> prescriptionProducts) {
            this.prescriptions = prescriptions;
            this.prescriptionProducts = prescriptionProducts;
            prepare();
        }

        private void prepare() {
            Set<String> customerCodes = new HashSet<>();
            Set<String> clinicCodes = new HashSet<>();
            for (Prescription prescription : prescriptions) {
                customerCodes.add(prescription.getCustomerCode());
                clinicCodes.add(prescription.getClinicCode());
            }
            this.customerMap = customerMapper.mapByCodes(customerCodes);
            this.clinicMap = clinicMapper.mapByCodes(clinicCodes);

            if (prescriptionProducts == null || prescriptionProducts.isEmpty()) {
                this.productGroups = Collections.emptyMap();
                this.skuMap = Collections.emptyMap();
            } else {
                this.productGroups = prescriptionProducts.stream()
                        .collect(Collectors.groupingBy(PrescriptionProduct::getPrescriptionCode));
                Set<String> skuCodes = prescriptionProducts.stream()
                        .map(PrescriptionProduct::getSkuCode)
                        .collect(Collectors.toSet());
                this.skuMap = productSkuRepository.mapByCodes(skuCodes);
            }
        }

        public Clinic getClinic(String clinicCode) {
            return clinicMap.get(clinicCode);
        }

        public Customer getCustomer(String customerCode) {
            return customerMap.get(customerCode);
        }

        public List<PrescriptionProduct> getPrescriptionProducts(String prescriptionCode) {
            return productGroups.getOrDefault(prescriptionCode, Collections.emptyList());
        }

        public ProductSkuVO getSku(String skuCode) {
            return skuMap.get(skuCode);
        }
    }

}
