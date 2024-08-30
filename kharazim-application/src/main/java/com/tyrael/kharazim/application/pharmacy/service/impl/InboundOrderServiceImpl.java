package com.tyrael.kharazim.application.pharmacy.service.impl;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.pharmacy.converter.InboundOrderConverter;
import com.tyrael.kharazim.application.pharmacy.domain.InboundOrder;
import com.tyrael.kharazim.application.pharmacy.domain.InboundOrderItem;
import com.tyrael.kharazim.application.pharmacy.enums.InboundOrderSourceType;
import com.tyrael.kharazim.application.pharmacy.enums.InboundOrderStatus;
import com.tyrael.kharazim.application.pharmacy.mapper.InboundOrderItemMapper;
import com.tyrael.kharazim.application.pharmacy.mapper.InboundOrderMapper;
import com.tyrael.kharazim.application.pharmacy.service.InboundOrderService;
import com.tyrael.kharazim.application.pharmacy.service.InventoryService;
import com.tyrael.kharazim.application.pharmacy.vo.inboundorder.AddInboundRequest;
import com.tyrael.kharazim.application.pharmacy.vo.inboundorder.InboundOrderVO;
import com.tyrael.kharazim.application.pharmacy.vo.inboundorder.PageInboundOrderRequest;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.InventoryChangeCommand;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.InventoryInboundCommand;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrder;
import com.tyrael.kharazim.application.purchase.event.PurchaseOrderReceivedEvent;
import com.tyrael.kharazim.application.purchase.vo.PurchaseOrderReceivedVO;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import com.tyrael.kharazim.common.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
    private final InventoryService inventoryService;
    private final InboundOrderConverter inboundOrderConverter;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFromPurchaseOrder(PurchaseOrder purchaseOrder) {
        InboundOrder inboundOrder = new InboundOrder();

        inboundOrder.setCode(codeGenerator.dailyNext(BusinessCodeConstants.INBOUND_ORDER));
        inboundOrder.setSourceBusinessCode(purchaseOrder.getCode());
        inboundOrder.setClinicCode(purchaseOrder.getClinicCode());
        inboundOrder.setSupplierCode(purchaseOrder.getSupplierCode());
        inboundOrder.setSourceRemark(purchaseOrder.getRemark());
        inboundOrder.setSourceType(InboundOrderSourceType.PURCHASE_ORDER);
        inboundOrder.setStatus(InboundOrderStatus.WAIT_INBOUND);

        List<InboundOrderItem> inboundOrderItems = purchaseOrder.getItems()
                .stream()
                .map(e -> {
                    InboundOrderItem inboundOrderItem = new InboundOrderItem();
                    inboundOrderItem.setInboundOrderCode(inboundOrder.getCode());
                    inboundOrderItem.setSkuCode(e.getSkuCode());
                    inboundOrderItem.setQuantity(e.getQuantity());
                    inboundOrderItem.setInboundedQuantity(0);
                    return inboundOrderItem;
                })
                .collect(Collectors.toList());
        inboundOrder.setItems(inboundOrderItems);

        this.save(inboundOrder);
    }

    @Override
    public PageResponse<InboundOrderVO> page(PageInboundOrderRequest pageRequest) {
        PageResponse<InboundOrder> pageData = inboundOrderMapper.page(pageRequest);

        Collection<InboundOrder> inboundOrders = pageData.getData();
        List<String> inboundOrderCodes = CollectionUtils.safeStream(inboundOrders)
                .map(InboundOrder::getCode)
                .toList();
        List<InboundOrderItem> inboundOrderItems = inboundOrderItemMapper.listByInboundOrderCodes(inboundOrderCodes);

        return PageResponse.success(
                inboundOrderConverter.inboundOrderVOs(inboundOrders, inboundOrderItems),
                pageData.getTotalCount(),
                pageData.getPageSize(),
                pageData.getPageNum());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inbound(AddInboundRequest addInboundRequest, AuthUser operator) {
        InboundOrder inboundOrder = exactlyFindByCodeForUpdate(addInboundRequest.getInboundOrderCode());
        BusinessException.assertFalse(InboundOrderStatus.INBOUND_FINISHED.equals(inboundOrder.getStatus()),
                "入库单已收货完成");

        Map<String, InboundOrderItem> skuItemMap = inboundOrder.getItems()
                .stream()
                .collect(Collectors.toMap(InboundOrderItem::getSkuCode, e -> e));
        for (AddInboundRequest.InboundItem item : addInboundRequest.getItems()) {
            InboundOrderItem inboundOrderItem = skuItemMap.get(item.getSkuCode());
            BusinessException.assertTrue(inboundOrderItem != null, "商品信息有误");
            inboundOrderItem.addInboundQuantity(item.getQuantity());
        }
        inboundOrder.refreshStatus();

        this.save(inboundOrder);

        String serialCode = codeGenerator.dailyNext(BusinessCodeConstants.INVENTORY_INBOUND);

        // 库存入库
        this.inventoryInbound(inboundOrder, addInboundRequest, serialCode, operator);

        // 通知采购单已收货
        this.notifyPurchaseOrderReceived(inboundOrder, addInboundRequest, serialCode, operator);
    }

    private void inventoryInbound(InboundOrder inboundOrder,
                                  AddInboundRequest addInboundRequest,
                                  String serialCode,
                                  AuthUser operator) {
        List<InventoryChangeCommand.Item> inboundItems = addInboundRequest.getItems()
                .stream()
                .map(e -> new InventoryChangeCommand.Item(e.getSkuCode(), e.getQuantity()))
                .collect(Collectors.toList());
        InventoryInboundCommand inventoryChangeCommand = new InventoryInboundCommand(
                inboundOrder.getCode(),
                serialCode,
                inboundOrder.getClinicCode(),
                inboundItems,
                operator.getNickName(),
                operator.getCode());
        inventoryService.inbound(inventoryChangeCommand);
    }

    private void notifyPurchaseOrderReceived(InboundOrder inboundOrder,
                                             AddInboundRequest addInboundRequest,
                                             String serialCode,
                                             AuthUser operator) {
        List<PurchaseOrderReceivedVO.Item> items = addInboundRequest.getItems()
                .stream()
                .map(e -> new PurchaseOrderReceivedVO.Item(e.getSkuCode(), e.getQuantity()))
                .collect(Collectors.toList());
        PurchaseOrderReceivedVO receivedVO = new PurchaseOrderReceivedVO(
                inboundOrder.getSourceBusinessCode(),
                serialCode,
                items,
                operator.getNickName(),
                operator.getCode());

        publisher.publishEvent(new PurchaseOrderReceivedEvent(this, receivedVO));
    }

    private InboundOrder exactlyFindByCodeForUpdate(String code) {
        InboundOrder inboundOrder = inboundOrderMapper.findByCodeForUpdate(code);
        DomainNotFoundException.assertFound(inboundOrder, code);
        inboundOrder.setItems(inboundOrderItemMapper.listByInboundOrderCode(code));
        return inboundOrder;
    }

    private void save(InboundOrder inboundOrder) {
        if (inboundOrder.getId() == null) {
            inboundOrderMapper.insert(inboundOrder);
            inboundOrderItemMapper.batchInsert(inboundOrder.getItems());
        } else {
            inboundOrderMapper.updateById(inboundOrder);
            for (InboundOrderItem item : inboundOrder.getItems()) {
                inboundOrderItemMapper.updateById(item);
            }
        }
    }

}
