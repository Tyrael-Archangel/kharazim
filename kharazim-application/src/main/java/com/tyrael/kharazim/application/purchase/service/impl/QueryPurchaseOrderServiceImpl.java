package com.tyrael.kharazim.application.purchase.service.impl;

import com.tyrael.kharazim.application.purchase.converter.PurchaseOrderConverter;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrder;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrderPaymentRecord;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrderReceiveRecord;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrderReceiveRecordItem;
import com.tyrael.kharazim.application.purchase.mapper.*;
import com.tyrael.kharazim.application.purchase.service.QueryPurchaseOrderService;
import com.tyrael.kharazim.application.purchase.vo.PurchaseOrderVO;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import com.tyrael.kharazim.common.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Service
@RequiredArgsConstructor
public class QueryPurchaseOrderServiceImpl implements QueryPurchaseOrderService {

    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PurchaseOrderItemMapper purchaseOrderItemMapper;
    private final PurchaseOrderPaymentRecordMapper paymentRecordMapper;
    private final PurchaseOrderReceiveRecordMapper receiveRecordMapper;
    private final PurchaseOrderReceiveRecordItemMapper receiveRecordItemMapper;
    private final PurchaseOrderConverter purchaseOrderConverter;

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderVO detail(String code) {
        PurchaseOrder purchaseOrder = this.findByCode(code);
        DomainNotFoundException.assertFound(purchaseOrder, code);

        return purchaseOrderConverter.purchaseOrderVO(
                purchaseOrder,
                this.paymentRecords(code),
                this.receiveRecords(code));
    }

    private PurchaseOrder findByCode(String purchaseOrderCode) {
        PurchaseOrder purchaseOrder = purchaseOrderMapper.findByCode(purchaseOrderCode);
        if (purchaseOrder == null) {
            return null;
        }
        purchaseOrder.setItems(purchaseOrderItemMapper.listByPurchaseOrderCode(purchaseOrderCode));
        return purchaseOrder;
    }

    private List<PurchaseOrderPaymentRecord> paymentRecords(String purchaseOrderCode) {
        return paymentRecordMapper.listByPurchaseOrderCode(purchaseOrderCode);
    }

    private List<PurchaseOrderReceiveRecord> receiveRecords(String purchaseOrderCode) {
        List<PurchaseOrderReceiveRecord> receiveRecords = receiveRecordMapper.listByPurchaseOrderCode(purchaseOrderCode);
        List<String> serialCodes = CollectionUtils.safeStream(receiveRecords)
                .map(PurchaseOrderReceiveRecord::getSerialCode)
                .collect(Collectors.toList());

        List<PurchaseOrderReceiveRecordItem> receiveRecordItems = receiveRecordItemMapper.listBySerialCodes(serialCodes);
        Map<String, List<PurchaseOrderReceiveRecordItem>> receiveRecordItemGroups = CollectionUtils.safeStream(receiveRecordItems)
                .collect(Collectors.groupingBy(PurchaseOrderReceiveRecordItem::getReceiveSerialCode));

        for (PurchaseOrderReceiveRecord receiveRecord : CollectionUtils.safeList(receiveRecords)) {
            List<PurchaseOrderReceiveRecordItem> recordItems = receiveRecordItemGroups.getOrDefault(
                    receiveRecord.getSerialCode(), new ArrayList<>());
            receiveRecord.setRecordItems(recordItems);
        }
        return receiveRecords;
    }

}
