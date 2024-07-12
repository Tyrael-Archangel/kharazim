package com.tyrael.kharazim.application.pharmacy.service.impl;

import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.pharmacy.domain.InboundOrder;
import com.tyrael.kharazim.application.pharmacy.domain.InboundOrderItem;
import com.tyrael.kharazim.application.pharmacy.enums.InboundOrderStatus;
import com.tyrael.kharazim.application.pharmacy.mapper.InboundOrderItemMapper;
import com.tyrael.kharazim.application.pharmacy.mapper.InboundOrderMapper;
import com.tyrael.kharazim.application.pharmacy.service.InboundOrderService;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrder;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/7/3
 */
@Service
@RequiredArgsConstructor
public class InboundOrderServiceImpl implements InboundOrderService {

    private final InboundOrderMapper inboundOrderMapper;
    private final InboundOrderItemMapper inboundOrderItemMapper;
    private final CodeGenerator codeGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFromPurchaseOrder(PurchaseOrder purchaseOrder) {
        InboundOrder inboundOrder = new InboundOrder();

        inboundOrder.setCode(codeGenerator.dailyNext(BusinessCodeConstants.INBOUND_ORDER));
        inboundOrder.setSourcePurchaseOrderCode(purchaseOrder.getCode());
        inboundOrder.setClinicCode(purchaseOrder.getClinicCode());
        inboundOrder.setSupplierCode(purchaseOrder.getSupplierCode());
        inboundOrder.setSourcePurchaseOrderCode(purchaseOrder.getRemark());
        inboundOrder.setStatus(InboundOrderStatus.WAIT_RECEIVE);

        List<InboundOrderItem> inboundOrderItems = purchaseOrder.getItems()
                .stream()
                .map(e -> {
                    InboundOrderItem inboundOrderItem = new InboundOrderItem();
                    inboundOrderItem.setInboundOrderCode(inboundOrder.getCode());
                    inboundOrderItem.setSkuCode(e.getSkuCode());
                    inboundOrderItem.setQuantity(e.getQuantity());
                    inboundOrderItem.setReceivedQuantity(0);
                    return inboundOrderItem;
                })
                .collect(Collectors.toList());
        inboundOrder.setItems(inboundOrderItems);

        this.save(inboundOrder);
    }

    private void save(InboundOrder inboundOrder) {
        inboundOrderMapper.insert(inboundOrder);
        inboundOrderItemMapper.batchInsert(inboundOrder.getItems());
    }

}
