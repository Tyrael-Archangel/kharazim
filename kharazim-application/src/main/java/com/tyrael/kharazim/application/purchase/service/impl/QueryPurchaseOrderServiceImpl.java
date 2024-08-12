package com.tyrael.kharazim.application.purchase.service.impl;

import com.tyrael.kharazim.application.purchase.converter.PurchaseOrderConverter;
import com.tyrael.kharazim.application.purchase.domain.*;
import com.tyrael.kharazim.application.purchase.mapper.*;
import com.tyrael.kharazim.application.purchase.service.QueryPurchaseOrderService;
import com.tyrael.kharazim.application.purchase.vo.PurchaseOrderVO;
import com.tyrael.kharazim.application.purchase.vo.request.PagePurchaseOrderRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import com.tyrael.kharazim.common.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
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
    public PageResponse<PurchaseOrderVO> page(PagePurchaseOrderRequest pageRequest) {
        PageResponse<PurchaseOrder> purchaseOrderPage = purchaseOrderMapper.page(pageRequest);
        Collection<PurchaseOrder> purchaseOrders = purchaseOrderPage.getData();
        List<String> purchaseOrderCodes = purchaseOrders.stream()
                .map(PurchaseOrder::getCode)
                .toList();

        List<PurchaseOrderVO> pageData = purchaseOrderConverter.purchaseOrderVOs(
                purchaseOrders,
                purchaseOrderItemMapper.listByPurchaseOrderCodes(purchaseOrderCodes),
                paymentRecordMapper.listByPurchaseOrderCodes(purchaseOrderCodes),
                receiveRecordMapper.listByPurchaseOrderCodes(purchaseOrderCodes));
        return PageResponse.success(
                pageData,
                purchaseOrderPage.getTotalCount(),
                purchaseOrderPage.getPageSize(),
                purchaseOrderPage.getPageNum());
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderVO detail(String code) {
        PurchaseOrder purchaseOrder = purchaseOrderMapper.findByCode(code);
        DomainNotFoundException.assertFound(purchaseOrder, code);

        return purchaseOrderConverter.purchaseOrderVO(
                purchaseOrder,
                this.purchaseOrderItems(code),
                this.paymentRecords(code),
                this.receiveRecords(code));
    }

    private List<PurchaseOrderItem> purchaseOrderItems(String purchaseOrderCode) {
        return purchaseOrderItemMapper.listByPurchaseOrderCode(purchaseOrderCode);
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
