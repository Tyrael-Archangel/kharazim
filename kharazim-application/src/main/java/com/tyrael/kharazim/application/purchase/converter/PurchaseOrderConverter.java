package com.tyrael.kharazim.application.purchase.converter;

import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.product.service.ProductSkuRepository;
import com.tyrael.kharazim.application.product.vo.sku.ProductSkuVO;
import com.tyrael.kharazim.application.purchase.domain.*;
import com.tyrael.kharazim.application.purchase.vo.PurchaseOrderVO;
import com.tyrael.kharazim.application.supplier.domain.SupplierDO;
import com.tyrael.kharazim.application.supplier.mapper.SupplierMapper;
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
    private final ProductSkuRepository productSkuRepository;

    /**
     * PurchaseOrder、PurchaseOrderItems、PurchaseOrderPaymentRecords、PurchaseOrderReceiveRecords -> PurchaseOrderVO
     */
    public PurchaseOrderVO purchaseOrderVO(PurchaseOrder purchaseOrder,
                                           List<PurchaseOrderItem> purchaseOrderItems,
                                           List<PurchaseOrderPaymentRecord> paymentRecords,
                                           List<PurchaseOrderReceiveRecord> receiveRecords) {
        if (purchaseOrder == null) {
            return null;
        }
        return purchaseOrderVOs(Collections.singletonList(purchaseOrder), purchaseOrderItems, paymentRecords, receiveRecords)
                .get(0);
    }

    /**
     * PurchaseOrders、PurchaseOrderItems、PurchaseOrderPaymentRecords、PurchaseOrderReceiveRecords -> PurchaseOrderVOs
     */
    public List<PurchaseOrderVO> purchaseOrderVOs(Collection<PurchaseOrder> purchaseOrders,
                                                  List<PurchaseOrderItem> purchaseOrderItems,
                                                  List<PurchaseOrderPaymentRecord> paymentRecords,
                                                  List<PurchaseOrderReceiveRecord> receiveRecords) {

        ConverterHelper converterHelper = new ConverterHelper(purchaseOrders, purchaseOrderItems, receiveRecords);

        Map<String, List<PurchaseOrderItem>> purchaseOrderItemGroups = CollectionUtils.safeStream(purchaseOrderItems)
                .collect(Collectors.groupingBy(PurchaseOrderItem::getPurchaseOrderCode));
        Map<String, List<PurchaseOrderPaymentRecord>> purchaseOrderPaymentGroups = CollectionUtils.safeStream(paymentRecords)
                .collect(Collectors.groupingBy(PurchaseOrderPaymentRecord::getPurchaseOrderCode));
        Map<String, List<PurchaseOrderReceiveRecord>> purchaseOrderReceiveGroups = CollectionUtils.safeStream(receiveRecords)
                .collect(Collectors.groupingBy(PurchaseOrderReceiveRecord::getPurchaseOrderCode));

        return purchaseOrders.stream()
                .map(purchaseOrder -> this.purchaseOrderVO(
                        purchaseOrder,
                        purchaseOrderItemGroups.get(purchaseOrder.getCode()),
                        purchaseOrderPaymentGroups.get(purchaseOrder.getCode()),
                        purchaseOrderReceiveGroups.get(purchaseOrder.getCode()),
                        converterHelper))
                .collect(Collectors.toList());
    }

    private PurchaseOrderVO purchaseOrderVO(PurchaseOrder purchaseOrder,
                                            List<PurchaseOrderItem> purchaseOrderItems,
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
                .items(this.purchaseOrderItemVOs(purchaseOrderItems, converterHelper))
                .paymentRecords(this.paymentRecordVOs(paymentRecords))
                .receiveRecords(this.receiveRecordVOs(receiveRecords, converterHelper))
                .createTime(purchaseOrder.getCreateTime())
                .creator(purchaseOrder.getCreator())
                .creatorCode(purchaseOrder.getCreatorCode())
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
                            .defaultImage(skuVO.getDefaultImage())
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
            List<PurchaseOrderPaymentRecord> paymentRecords) {
        return CollectionUtils.safeStream(paymentRecords)
                .map(paymentRecord -> PurchaseOrderVO.PurchaseOrderPaymentRecordVO.builder()
                        .serialCode(paymentRecord.getSerialCode())
                        .amount(paymentRecord.getAmount())
                        .paymentTime(paymentRecord.getPaymentTime())
                        .paymentUser(paymentRecord.getPaymentUser())
                        .paymentUserCode(paymentRecord.getPaymentUserCode())
                        .vouchers(paymentRecord.getVouchers())
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
                            .defaultImage(skuVO.getDefaultImage())
                            .build();
                }).collect(Collectors.toList());
    }

    private class ConverterHelper {

        private final Collection<PurchaseOrder> purchaseOrders;
        private final List<PurchaseOrderItem> purchaseOrderItems;
        private final List<PurchaseOrderReceiveRecord> receiveRecords;

        private Map<String, Clinic> clinicMap;
        private Map<String, SupplierDO> supplierMap;
        private Map<String, ProductSkuVO> skuMap;

        ConverterHelper(Collection<PurchaseOrder> purchaseOrders,
                        List<PurchaseOrderItem> purchaseOrderItems,
                        List<PurchaseOrderReceiveRecord> receiveRecords) {
            this.purchaseOrders = purchaseOrders;
            this.purchaseOrderItems = purchaseOrderItems;
            this.receiveRecords = receiveRecords;
            this.prepare();
        }

        private void prepare() {

            Set<String> clinicCodes = new HashSet<>();
            Set<String> supplierCodes = new HashSet<>();
            for (PurchaseOrder purchaseOrder : CollectionUtils.safeCollection(purchaseOrders)) {
                clinicCodes.add(purchaseOrder.getClinicCode());
                supplierCodes.add(purchaseOrder.getSupplierCode());
            }
            this.clinicMap = clinicMapper.mapByCodes(clinicCodes);
            this.supplierMap = supplierMapper.mapByCodes(supplierCodes);

            Stream<String> purchaseOrderItemSkuStream = CollectionUtils.safeStream(purchaseOrderItems)
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

    }

}
