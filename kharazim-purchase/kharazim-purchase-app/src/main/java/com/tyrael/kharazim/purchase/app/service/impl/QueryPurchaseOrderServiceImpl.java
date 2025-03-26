package com.tyrael.kharazim.purchase.app.service.impl;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.purchase.app.converter.PurchaseOrderConverter;
import com.tyrael.kharazim.purchase.app.domain.*;
import com.tyrael.kharazim.purchase.app.mapper.*;
import com.tyrael.kharazim.purchase.app.service.QueryPurchaseOrderService;
import com.tyrael.kharazim.purchase.app.vo.purchaseorder.PagePurchaseOrderRequest;
import com.tyrael.kharazim.purchase.app.vo.purchaseorder.PurchaseOrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    public PageResponse<PurchaseOrderVO> page(PagePurchaseOrderRequest pageRequest) {
        PageResponse<PurchaseOrder> purchaseOrderPage = purchaseOrderMapper.page(pageRequest);
        Collection<PurchaseOrder> purchaseOrders = purchaseOrderPage.getData();
        List<String> purchaseOrderCodes = purchaseOrders.stream()
                .map(PurchaseOrder::getCode)
                .toList();

        List<PurchaseOrderVO> pageData = purchaseOrderConverter.purchaseOrderVOs(
                purchaseOrders,
                this.purchaseOrderItems(purchaseOrderCodes),
                this.paymentRecords(purchaseOrderCodes),
                this.receiveRecords(purchaseOrderCodes));
        return PageResponse.success(pageData, purchaseOrderPage.getTotalCount());
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderVO detail(String code) {
        PurchaseOrder purchaseOrder = purchaseOrderMapper.findByCode(code);
        DomainNotFoundException.assertFound(purchaseOrder, code);

        return purchaseOrderConverter.purchaseOrderVO(
                purchaseOrder,
                this.purchaseOrderItems(Collections.singletonList(code)),
                this.paymentRecords(Collections.singletonList(code)),
                this.receiveRecords(Collections.singletonList(code)));
    }

    private List<PurchaseOrderItem> purchaseOrderItems(Collection<String> purchaseOrderCodes) {
        return purchaseOrderItemMapper.listByPurchaseOrderCodes(purchaseOrderCodes);
    }

    private List<PurchaseOrderPaymentRecord> paymentRecords(Collection<String> purchaseOrderCodes) {
        return paymentRecordMapper.listByPurchaseOrderCodes(purchaseOrderCodes);
    }

    private List<PurchaseOrderReceiveRecord> receiveRecords(Collection<String> purchaseOrderCodes) {
        List<PurchaseOrderReceiveRecord> receiveRecords = receiveRecordMapper.listByPurchaseOrderCodes(purchaseOrderCodes);
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
