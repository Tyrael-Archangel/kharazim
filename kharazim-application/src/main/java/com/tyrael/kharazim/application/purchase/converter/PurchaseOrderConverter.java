package com.tyrael.kharazim.application.purchase.converter;

import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.product.service.ProductSkuRepository;
import com.tyrael.kharazim.application.product.vo.sku.ProductSkuVO;
import com.tyrael.kharazim.application.purchase.domain.*;
import com.tyrael.kharazim.application.purchase.vo.PurchaseOrderVO;
import com.tyrael.kharazim.application.supplier.domain.SupplierDO;
import com.tyrael.kharazim.application.supplier.mapper.SupplierMapper;
import com.tyrael.kharazim.application.system.dto.file.FileVO;
import com.tyrael.kharazim.application.system.service.FileService;
import com.tyrael.kharazim.common.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Component
@RequiredArgsConstructor
public class PurchaseOrderConverter {

    private final ClinicMapper clinicMapper;
    private final SupplierMapper supplierMapper;
    private final FileService fileService;
    private final ProductSkuRepository productSkuRepository;

    /**
     * PurchaseOrder、PurchaseOrderPaymentRecords、PurchaseOrderReceiveRecords -> PurchaseOrderVO
     */
    public PurchaseOrderVO purchaseOrderVO(PurchaseOrder purchaseOrder,
                                           List<PurchaseOrderPaymentRecord> paymentRecords,
                                           List<PurchaseOrderReceiveRecord> receiveRecords) {
        if (purchaseOrder == null) {
            return null;
        }
        return purchaseOrderVOs(Collections.singletonList(purchaseOrder), paymentRecords, receiveRecords)
                .get(0);
    }

    /**
     * PurchaseOrders、PurchaseOrderPaymentRecords、PurchaseOrderReceiveRecords -> PurchaseOrderVOs
     */
    public List<PurchaseOrderVO> purchaseOrderVOs(List<PurchaseOrder> purchaseOrders,
                                                  List<PurchaseOrderPaymentRecord> paymentRecords,
                                                  List<PurchaseOrderReceiveRecord> receiveRecords) {

        ConverterHelper converterHelper = new ConverterHelper(purchaseOrders, paymentRecords, receiveRecords);

        Map<String, List<PurchaseOrderPaymentRecord>> purchaseOrderPaymentGroups = CollectionUtils.safeStream(paymentRecords)
                .collect(Collectors.groupingBy(PurchaseOrderPaymentRecord::getPurchaseOrderCode));
        Map<String, List<PurchaseOrderReceiveRecord>> purchaseOrderReceiveGroups = CollectionUtils.safeStream(receiveRecords)
                .collect(Collectors.groupingBy(PurchaseOrderReceiveRecord::getPurchaseOrderCode));

        return purchaseOrders.stream()
                .map(purchaseOrder -> this.purchaseOrderVO(
                        purchaseOrder,
                        purchaseOrderPaymentGroups.get(purchaseOrder.getCode()),
                        purchaseOrderReceiveGroups.get(purchaseOrder.getCode()),
                        converterHelper))
                .collect(Collectors.toList());
    }

    private PurchaseOrderVO purchaseOrderVO(PurchaseOrder purchaseOrder,
                                            List<PurchaseOrderPaymentRecord> paymentRecords,
                                            List<PurchaseOrderReceiveRecord> receiveRecords,
                                            ConverterHelper converterHelper) {

        Clinic clinic = converterHelper.getClinic(purchaseOrder.getClinicCode());
        SupplierDO supplier = converterHelper.getSupplier(purchaseOrder.getSupplierCode());

        return PurchaseOrderVO.builder()
                .code(purchaseOrder.getCode())
                .clinicCode(clinic.getCode())
                .clinicName(clinic.getName())
                .supplierCode(supplier.getCode())
                .supplierName(supplier.getName())
                .totalAmount(purchaseOrder.getTotalAmount())
                .paidAmount(purchaseOrder.getPaidAmount())
                .receiveStatus(purchaseOrder.getReceiveStatus())
                .paymentStatus(purchaseOrder.getPaymentStatus())
                .remark(purchaseOrder.getRemark())
                .items(this.purchaseOrderItemVOs(purchaseOrder.getItems(), converterHelper))
                .paymentRecords(this.paymentRecordVOs(paymentRecords, converterHelper))
                .receiveRecords(this.receiveRecordVOs(receiveRecords, converterHelper))
                .build();

    }

    private List<PurchaseOrderVO.PurchaseOrderItemVO> purchaseOrderItemVOs(List<PurchaseOrderItem> items,
                                                                           ConverterHelper converterHelper) {
        if (items == null || items.isEmpty()) {
            return new ArrayList<>();
        }

        return items.stream()
                .map(item -> {
                    ProductSkuVO skuVO = converterHelper.getSku(item.getSkuCode());
                    return PurchaseOrderVO.PurchaseOrderItemVO.builder()
                            .skuCode(item.getSkuCode())
                            .skuName(skuVO.getName())
                            .categoryCode(skuVO.getCategoryCode())
                            .categoryName(skuVO.getCategoryName())
                            .unitCode(skuVO.getUnitCode())
                            .unitName(skuVO.getUnitName())
                            .defaultImageUrl(skuVO.getDefaultImageUrl())
                            .description(skuVO.getDescription())
                            .quantity(item.getQuantity())
                            .receivedQuantity(item.getReceivedQuantity())
                            .price(item.getPrice())
                            .amount(item.getAmount())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<PurchaseOrderVO.PurchaseOrderPaymentRecordVO> paymentRecordVOs(
            List<PurchaseOrderPaymentRecord> paymentRecords, ConverterHelper converterHelper) {
        return CollectionUtils.safeStream(paymentRecords)
                .map(paymentRecord -> PurchaseOrderVO.PurchaseOrderPaymentRecordVO.builder()
                        .serialCode(paymentRecord.getSerialCode())
                        .amount(paymentRecord.getAmount())
                        .paymentTime(paymentRecord.getPaymentTime())
                        .paymentUser(paymentRecord.getPaymentUser())
                        .paymentUserCode(paymentRecord.getPaymentUserCode())
                        .vouchers(paymentRecord.getVouchers())
                        .voucherUrls(converterHelper.getFileUrls(paymentRecord.getVouchers()))
                        .voucherFiles(converterHelper.getFiles(paymentRecord.getVouchers()))
                        .build())
                .collect(Collectors.toList());
    }

    private List<PurchaseOrderVO.PurchaseOrderReceiveRecordVO> receiveRecordVOs(
            List<PurchaseOrderReceiveRecord> receiveRecords, ConverterHelper converterHelper) {
        return CollectionUtils.safeStream(receiveRecords)
                .map(receiveRecord -> PurchaseOrderVO.PurchaseOrderReceiveRecordVO.builder()
                        .serialCode(receiveRecord.getSerialCode())
                        .trackingNumber(receiveRecord.getTrackingNumber())
                        .receiveUser(receiveRecord.getReceiveUser())
                        .receiveUserCode(receiveRecord.getReceiveUserCode())
                        .receiveTime(receiveRecord.getReceiveTime())
                        .receiveItems(this.receiveItemVOs(receiveRecord.getRecordItems(), converterHelper))
                        .build())
                .collect(Collectors.toList());
    }

    private List<PurchaseOrderVO.ReceiveRecordItemVO> receiveItemVOs(List<PurchaseOrderReceiveRecordItem> recordItems,
                                                                     ConverterHelper converterHelper) {
        return CollectionUtils.safeStream(recordItems)
                .map(recordItem -> {
                    ProductSkuVO skuVO = converterHelper.getSku(recordItem.getSkuCode());
                    return PurchaseOrderVO.ReceiveRecordItemVO.builder()
                            .skuCode(skuVO.getCode())
                            .skuName(skuVO.getName())
                            .quantity(recordItem.getQuantity())
                            .categoryCode(skuVO.getCategoryCode())
                            .categoryName(skuVO.getCategoryName())
                            .unitCode(skuVO.getUnitCode())
                            .unitName(skuVO.getUnitName())
                            .defaultImageUrl(skuVO.getDefaultImageUrl())
                            .build();
                }).collect(Collectors.toList());
    }

    private class ConverterHelper {

        private final List<PurchaseOrder> purchaseOrders;
        private final List<PurchaseOrderPaymentRecord> paymentRecords;
        private final List<PurchaseOrderReceiveRecord> receiveRecords;

        private Map<String, Clinic> clinicMap;
        private Map<String, SupplierDO> supplierMap;
        private Map<String, ProductSkuVO> skuMap;
        private Map<String, FileVO> fileMap;

        ConverterHelper(List<PurchaseOrder> purchaseOrders,
                        List<PurchaseOrderPaymentRecord> paymentRecords,
                        List<PurchaseOrderReceiveRecord> receiveRecords) {
            this.purchaseOrders = purchaseOrders;
            this.paymentRecords = paymentRecords;
            this.receiveRecords = receiveRecords;
            this.prepare();
        }

        private void prepare() {

            Set<String> clinicCodes = new HashSet<>();
            Set<String> supplierCodes = new HashSet<>();
            for (PurchaseOrder purchaseOrder : CollectionUtils.safeList(purchaseOrders)) {
                clinicCodes.add(purchaseOrder.getClinicCode());
                supplierCodes.add(purchaseOrder.getSupplierCode());
            }
            this.clinicMap = clinicMapper.mapByCodes(clinicCodes);
            this.supplierMap = supplierMapper.mapByCodes(supplierCodes);

            Stream<String> purchaseOrderItemSkuStream = CollectionUtils.safeStream(purchaseOrders)
                    .map(PurchaseOrder::getItems)
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .filter(Objects::nonNull)
                    .map(PurchaseOrderItem::getSkuCode);
            Stream<String> receiveSkuStream = CollectionUtils.safeStream(receiveRecords)
                    .map(PurchaseOrderReceiveRecord::getRecordItems)
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .filter(Objects::nonNull)
                    .map(PurchaseOrderReceiveRecordItem::getSkuCode);
            Set<String> skuCodes = Stream.concat(purchaseOrderItemSkuStream, receiveSkuStream)
                    .collect(Collectors.toSet());
            this.skuMap = productSkuRepository.mapByCodes(skuCodes);

            List<String> vouchers = CollectionUtils.safeStream(paymentRecords)
                    .map(PurchaseOrderPaymentRecord::getVouchers)
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
            this.fileMap = fileService.getFiles(vouchers)
                    .stream()
                    .collect(Collectors.toMap(FileVO::getFileId, e -> e));
        }

        public Clinic getClinic(String clinicCode) {
            return clinicMap.get(clinicCode);
        }

        public ProductSkuVO getSku(String skuCode) {
            return skuMap.get(skuCode);
        }

        public SupplierDO getSupplier(String supplierCode) {
            return supplierMap.get(supplierCode);
        }

        public List<FileVO> getFiles(List<String> vouchers) {
            return CollectionUtils.safeStream(vouchers)
                    .filter(Objects::nonNull)
                    .map(fileMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        public List<String> getFileUrls(List<String> vouchers) {
            return CollectionUtils.safeStream(vouchers)
                    .filter(Objects::nonNull)
                    .map(fileMap::get)
                    .filter(Objects::nonNull)
                    .map(FileVO::getUrl)
                    .collect(Collectors.toList());
        }
    }

}
