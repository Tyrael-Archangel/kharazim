package com.tyrael.kharazim.pharmacy.app.service;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
import com.tyrael.kharazim.mq.MqProducer;
import com.tyrael.kharazim.pharmacy.app.constant.PharmacyBusinessIdConstants;
import com.tyrael.kharazim.pharmacy.app.converter.InboundOrderConverter;
import com.tyrael.kharazim.pharmacy.app.domain.InboundOrder;
import com.tyrael.kharazim.pharmacy.app.domain.InboundOrderItem;
import com.tyrael.kharazim.pharmacy.app.enums.InboundOrderSourceType;
import com.tyrael.kharazim.pharmacy.app.enums.InboundOrderStatus;
import com.tyrael.kharazim.pharmacy.app.mapper.InboundOrderItemMapper;
import com.tyrael.kharazim.pharmacy.app.mapper.InboundOrderMapper;
import com.tyrael.kharazim.pharmacy.app.vo.inbound.AddInboundRequest;
import com.tyrael.kharazim.pharmacy.app.vo.inbound.InboundOrderVO;
import com.tyrael.kharazim.pharmacy.app.vo.inbound.PageInboundOrderRequest;
import com.tyrael.kharazim.pharmacy.app.vo.inventory.InventoryChangeCommand;
import com.tyrael.kharazim.pharmacy.app.vo.inventory.InventoryInboundCommand;
import com.tyrael.kharazim.pharmacy.sdk.model.message.CreatePurchaseInboundOrderMessage;
import com.tyrael.kharazim.pharmacy.sdk.model.message.InboundOrderReceivedMessage;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/4/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InboundOrderServiceImpl implements InboundOrderService {

    private final InboundOrderMapper inboundOrderMapper;
    private final InboundOrderItemMapper inboundOrderItemMapper;
    private final IdGenerator idGenerator;
    private final InventoryService inventoryService;
    private final InboundOrderConverter inboundOrderConverter;
    private final MqProducer mqProducer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFromPurchase(CreatePurchaseInboundOrderMessage message) {
        InboundOrder inboundOrder = new InboundOrder();

        inboundOrder.setCode(idGenerator.dailyNext(PharmacyBusinessIdConstants.INBOUND_ORDER));
        inboundOrder.setSourceBusinessCode(message.getSourceBusinessCode());
        inboundOrder.setClinicCode(message.getClinicCode());
        inboundOrder.setSupplierCode(message.getSupplierCode());
        inboundOrder.setSourceRemark(message.getSourceRemark());
        inboundOrder.setSourceType(InboundOrderSourceType.PURCHASE_ORDER);
        inboundOrder.setStatus(InboundOrderStatus.WAIT_INBOUND);

        List<InboundOrderItem> inboundOrderItems = message.getItems()
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
    @Transactional(readOnly = true)
    public PageResponse<InboundOrderVO> page(PageInboundOrderRequest pageRequest) {
        PageResponse<InboundOrder> pageData = inboundOrderMapper.page(pageRequest);

        Collection<InboundOrder> inboundOrders = pageData.getData();
        List<String> inboundOrderCodes = CollectionUtils.safeStream(inboundOrders)
                .map(InboundOrder::getCode)
                .toList();
        List<InboundOrderItem> inboundOrderItems = inboundOrderItemMapper.listByInboundOrderCodes(inboundOrderCodes);

        return PageResponse.success(
                inboundOrderConverter.inboundOrderVOs(inboundOrders, inboundOrderItems),
                pageData.getTotalCount());
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

        String serialCode = idGenerator.dailyNext(PharmacyBusinessIdConstants.INVENTORY_INBOUND);

        // 库存入库
        this.inventoryInbound(inboundOrder, addInboundRequest, serialCode, operator);

        // 发布已收货通知
        this.publishInboundReceived(inboundOrder, addInboundRequest, serialCode, operator);
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

    private void publishInboundReceived(InboundOrder inboundOrder,
                                        AddInboundRequest addInboundRequest,
                                        String serialCode,
                                        AuthUser operator) {

        List<InboundOrderReceivedMessage.Item> items = addInboundRequest.getItems()
                .stream()
                .map(e -> new InboundOrderReceivedMessage.Item()
                        .setSkuCode(e.getSkuCode())
                        .setQuantity(e.getQuantity()))
                .collect(Collectors.toList());
        InboundOrderReceivedMessage message = new InboundOrderReceivedMessage()
                .setSourceBusinessCode(inboundOrder.getSourceBusinessCode())
                .setSerialCode(serialCode)
                .setItems(items)
                .setOperatorCode(operator.getCode())
                .setOperator(operator.getNickName());

        mqProducer.send("INBOUND_ORDER_RECEIVED", message);
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
