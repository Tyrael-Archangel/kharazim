package com.tyrael.kharazim.purchase.app.service.impl;

import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
import com.tyrael.kharazim.purchase.app.constant.PurchaseBusinessIdConstants;
import com.tyrael.kharazim.purchase.app.domain.PurchaseOrder;
import com.tyrael.kharazim.purchase.app.domain.PurchaseOrderItem;
import com.tyrael.kharazim.purchase.app.domain.PurchaseOrderReceiveRecord;
import com.tyrael.kharazim.purchase.app.domain.PurchaseOrderReceiveRecordItem;
import com.tyrael.kharazim.purchase.app.mapper.PurchaseOrderItemMapper;
import com.tyrael.kharazim.purchase.app.mapper.PurchaseOrderMapper;
import com.tyrael.kharazim.purchase.app.mapper.PurchaseOrderReceiveRecordItemMapper;
import com.tyrael.kharazim.purchase.app.mapper.PurchaseOrderReceiveRecordMapper;
import com.tyrael.kharazim.purchase.app.service.PurchaseOrderReceivedService;
import com.tyrael.kharazim.purchase.app.vo.purchaseorder.PurchaseOrderReceivedVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/8/9
 */
@Service
@RequiredArgsConstructor
public class PurchaseOrderReceivedServiceImpl implements PurchaseOrderReceivedService {

    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PurchaseOrderItemMapper purchaseOrderItemMapper;
    private final PurchaseOrderReceiveRecordMapper receiveRecordMapper;
    private final PurchaseOrderReceiveRecordItemMapper receiveRecordItemMapper;
    private final IdGenerator idGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receive(PurchaseOrderReceivedVO receivedVO) {
        PurchaseOrder purchaseOrder = exactlyFindByCode(receivedVO.purchaseOrderCode());
        Map<String, PurchaseOrderItem> itemMap = purchaseOrder.getItems()
                .stream()
                .collect(Collectors.toMap(PurchaseOrderItem::getSkuCode, e -> e));
        for (PurchaseOrderReceivedVO.Item item : receivedVO.items()) {
            PurchaseOrderItem purchaseOrderItem = itemMap.get(item.skuCode());
            BusinessException.assertTrue(purchaseOrderItem != null, "Purchase order item not found");
            purchaseOrderItem.increaseReceive(item.quantity());
        }
        purchaseOrder.refreshReceiveStatus();

        this.saveReceived(purchaseOrder);

        this.saveReceiveRecord(receivedVO);
    }

    private void saveReceived(PurchaseOrder purchaseOrder) {
        purchaseOrderMapper.updateById(purchaseOrder);
        for (PurchaseOrderItem item : purchaseOrder.getItems()) {
            purchaseOrderItemMapper.updateById(item);
        }
    }

    private void saveReceiveRecord(PurchaseOrderReceivedVO receivedVO) {

        String receiveSerialCode = idGenerator.dailyNext(PurchaseBusinessIdConstants.PURCHASE_ORDER_RECEIVE);

        PurchaseOrderReceiveRecord receiveRecord = new PurchaseOrderReceiveRecord();
        receiveRecord.setSerialCode(receiveSerialCode);
        receiveRecord.setPurchaseOrderCode(receivedVO.purchaseOrderCode());
        receiveRecord.setTrackingNumber(receivedVO.serialCode());
        receiveRecord.setReceiveTime(LocalDateTime.now());
        receiveRecord.setReceiveUser(receivedVO.operator());
        receiveRecord.setReceiveUserCode(receivedVO.operatorCode());

        List<PurchaseOrderReceiveRecordItem> recordItems = receivedVO.items()
                .stream()
                .map(e -> {
                    PurchaseOrderReceiveRecordItem receiveRecordItem = new PurchaseOrderReceiveRecordItem();
                    receiveRecordItem.setReceiveSerialCode(receiveSerialCode);
                    receiveRecordItem.setSkuCode(e.skuCode());
                    receiveRecordItem.setQuantity(e.quantity());
                    return receiveRecordItem;
                })
                .collect(Collectors.toList());

        receiveRecord.setRecordItems(recordItems);

        receiveRecordMapper.insert(receiveRecord);
        receiveRecordItemMapper.batchInsert(recordItems);
    }

    private PurchaseOrder exactlyFindByCode(String purchaseOrderCode) {
        PurchaseOrder purchaseOrder = purchaseOrderMapper.findByCode(purchaseOrderCode);
        DomainNotFoundException.assertFound(purchaseOrder, purchaseOrderCode);
        purchaseOrder.setItems(purchaseOrderItemMapper.listByPurchaseOrderCode(purchaseOrderCode));
        return purchaseOrder;
    }

}
