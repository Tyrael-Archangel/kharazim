package com.tyrael.kharazim.purchase.app.service.impl;

import com.tyrael.kharazim.basicdata.sdk.service.ClinicServiceApi;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
import com.tyrael.kharazim.purchase.app.constant.PurchaseBusinessIdConstants;
import com.tyrael.kharazim.purchase.app.domain.PurchaseOrder;
import com.tyrael.kharazim.purchase.app.domain.PurchaseOrderItem;
import com.tyrael.kharazim.purchase.app.enums.PurchasePaymentStatus;
import com.tyrael.kharazim.purchase.app.enums.PurchaseReceiveStatus;
import com.tyrael.kharazim.purchase.app.mapper.PurchaseOrderItemMapper;
import com.tyrael.kharazim.purchase.app.mapper.PurchaseOrderMapper;
import com.tyrael.kharazim.purchase.app.mapper.SupplierMapper;
import com.tyrael.kharazim.purchase.app.service.CreatePurchaseOrderService;
import com.tyrael.kharazim.purchase.app.vo.purchaseorder.CreatePurchaseOrderRequest;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
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
    private final IdGenerator codeGenerator;
    private final SupplierMapper supplierMapper;
    private final ApplicationEventPublisher publisher;

    @DubboReference
    private ClinicServiceApi clinicServiceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(CreatePurchaseOrderRequest request, AuthUser currentUser) {

        clinicServiceApi.exactlyFindByCode(request.getClinicCode());
        supplierMapper.ensureSupplierExist(request.getSupplierCode());

        PurchaseOrder purchaseOrder = this.buildPurchaseOrder(request, currentUser);
        purchaseOrderMapper.insert(purchaseOrder);
        purchaseOrderItemMapper.batchInsert(purchaseOrder.getItems());

        // TODO
//        publisher.publishEvent(new CreateInboundOrderEvent(this, purchaseOrder));

        return purchaseOrder.getCode();
    }

    private PurchaseOrder buildPurchaseOrder(CreatePurchaseOrderRequest request, AuthUser currentUser) {
        String code = codeGenerator.dailyNext(PurchaseBusinessIdConstants.PURCHASE_ORDER);

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
        purchaseOrder.setReceiveStatus(PurchaseReceiveStatus.WAIT_RECEIVE);
        purchaseOrder.setPaymentStatus(PurchasePaymentStatus.UNPAID);
        purchaseOrder.setTotalAmount(totalAmount);
        purchaseOrder.setPaidAmount(BigDecimal.ZERO);
        purchaseOrder.setRemark(request.getRemark());
        purchaseOrder.setItems(items);
        purchaseOrder.setCreateUser(currentUser.getCode(), currentUser.getNickName());

        return purchaseOrder;
    }

}
