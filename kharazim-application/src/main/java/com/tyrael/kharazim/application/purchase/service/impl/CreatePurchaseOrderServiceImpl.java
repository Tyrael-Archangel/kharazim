package com.tyrael.kharazim.application.purchase.service.impl;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.pharmacy.event.CreateInboundOrderEvent;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrder;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrderItem;
import com.tyrael.kharazim.application.purchase.enums.PurchaseOrderPaymentStatus;
import com.tyrael.kharazim.application.purchase.enums.PurchaseOrderReceiveStatus;
import com.tyrael.kharazim.application.purchase.mapper.PurchaseOrderItemMapper;
import com.tyrael.kharazim.application.purchase.mapper.PurchaseOrderMapper;
import com.tyrael.kharazim.application.purchase.service.CreatePurchaseOrderService;
import com.tyrael.kharazim.application.purchase.vo.request.CreatePurchaseOrderRequest;
import com.tyrael.kharazim.application.supplier.mapper.SupplierMapper;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Service
@RequiredArgsConstructor
public class CreatePurchaseOrderServiceImpl implements CreatePurchaseOrderService {

    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PurchaseOrderItemMapper purchaseOrderItemMapper;
    private final CodeGenerator codeGenerator;
    private final ClinicMapper clinicMapper;
    private final SupplierMapper supplierMapper;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(CreatePurchaseOrderRequest request, AuthUser currentUser) {

        clinicMapper.exactlyFindByCode(request.getClinicCode());
        supplierMapper.ensureSupplierExist(request.getSupplierCode());

        PurchaseOrder purchaseOrder = this.buildPurchaseOrder(request, currentUser);
        purchaseOrderMapper.insert(purchaseOrder);
        purchaseOrderItemMapper.batchInsert(purchaseOrder.getItems());

        publisher.publishEvent(new CreateInboundOrderEvent(this, purchaseOrder));

        return purchaseOrder.getCode();
    }

    private PurchaseOrder buildPurchaseOrder(CreatePurchaseOrderRequest request, AuthUser currentUser) {
        String code = codeGenerator.dailyNext(BusinessCodeConstants.PURCHASE_ORDER);

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<PurchaseOrderItem> items = new ArrayList<>();
        for (CreatePurchaseOrderRequest.CreatePurchaseOrderItem item : request.getItems()) {
            Integer quantity = item.getQuantity();
            BigDecimal price = item.getPrice();
            BigDecimal amount = price.multiply(new BigDecimal(quantity));
            PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
            purchaseOrderItem.setPurchaseOrderCode(code);
            purchaseOrderItem.setSkuCode(item.getSkuCode());
            purchaseOrderItem.setQuantity(quantity);
            purchaseOrderItem.setReceivedQuantity(0);
            purchaseOrderItem.setPrice(price);
            purchaseOrderItem.setAmount(amount);

            totalAmount = totalAmount.add(amount);
            items.add(purchaseOrderItem);
        }

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setCode(code);
        purchaseOrder.setClinicCode(request.getClinicCode());
        purchaseOrder.setSupplierCode(request.getSupplierCode());
        purchaseOrder.setReceiveStatus(PurchaseOrderReceiveStatus.WAIT_RECEIVE);
        purchaseOrder.setPaymentStatus(PurchaseOrderPaymentStatus.UNPAID);
        purchaseOrder.setTotalAmount(totalAmount);
        purchaseOrder.setPaidAmount(BigDecimal.ZERO);
        purchaseOrder.setRemark(request.getRemark());
        purchaseOrder.setItems(items);
        purchaseOrder.setCreateUser(currentUser.getCode(), currentUser.getNickName());

        return purchaseOrder;
    }

}
