package com.tyrael.kharazim.pharmacy.app.service;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
import com.tyrael.kharazim.pharmacy.app.constant.PharmacyBusinessIdConstants;
import com.tyrael.kharazim.pharmacy.app.converter.OutboundOrderConverter;
import com.tyrael.kharazim.pharmacy.app.domain.OutboundOrder;
import com.tyrael.kharazim.pharmacy.app.domain.OutboundOrderItem;
import com.tyrael.kharazim.pharmacy.app.enums.OutboundOrderStatus;
import com.tyrael.kharazim.pharmacy.app.mapper.OutboundOrderItemMapper;
import com.tyrael.kharazim.pharmacy.app.mapper.OutboundOrderMapper;
import com.tyrael.kharazim.pharmacy.app.vo.inventory.InventoryChangeCommand;
import com.tyrael.kharazim.pharmacy.app.vo.inventory.InventoryOutboundCommand;
import com.tyrael.kharazim.pharmacy.app.vo.outbound.OutboundOrderVO;
import com.tyrael.kharazim.pharmacy.app.vo.outbound.OutboundRequest;
import com.tyrael.kharazim.pharmacy.app.vo.outbound.PageOutboundOrderRequest;
import com.tyrael.kharazim.pharmacy.sdk.model.message.CreatePrescriptionOutboundOrderMessage;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@Service
@RequiredArgsConstructor
public class OutboundOrderServiceImpl implements OutboundOrderService {

    private final IdGenerator codeGenerator;
    private final OutboundOrderMapper outboundOrderMapper;
    private final OutboundOrderItemMapper outboundOrderItemMapper;
    private final OutboundOrderConverter outboundOrderConverter;
    private final InventoryService inventoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFromPrescription(CreatePrescriptionOutboundOrderMessage message) {
        OutboundOrder outboundOrder = new OutboundOrder();

        outboundOrder.setCode(codeGenerator.dailyNext(PharmacyBusinessIdConstants.OUTBOUND_ORDER));
        outboundOrder.setStatus(OutboundOrderStatus.WAIT_OUTBOUND);
        outboundOrder.setSourceBusinessCode(message.getSourceBusinessCode());
        outboundOrder.setCustomerCode(message.getCustomerCode());
        outboundOrder.setClinicCode(message.getClinicCode());
        outboundOrder.setSourceRemark(message.getRemark());

        List<OutboundOrderItem> outboundOrderItems = message.getItems()
                .stream()
                .map(e -> {
                    OutboundOrderItem outboundOrderItem = new OutboundOrderItem();
                    outboundOrderItem.setOutboundOrderCode(outboundOrder.getCode());
                    outboundOrderItem.setSkuCode(e.getSkuCode());
                    outboundOrderItem.setQuantity(e.getQuantity());
                    return outboundOrderItem;
                })
                .collect(Collectors.toList());
        outboundOrder.setItems(outboundOrderItems);

        this.save(outboundOrder);
    }

    @Override
    public PageResponse<OutboundOrderVO> page(PageOutboundOrderRequest pageRequest) {
        PageResponse<OutboundOrder> pageData = outboundOrderMapper.page(pageRequest);

        Collection<OutboundOrder> outboundOrders = pageData.getData();
        List<String> outboundOrderCodes = outboundOrders.stream()
                .map(OutboundOrder::getCode)
                .toList();
        List<OutboundOrderItem> outboundOrderItems = outboundOrderItemMapper.listByOutboundOrderCodes(outboundOrderCodes);

        return PageResponse.success(
                outboundOrderConverter.outboundOrderVOs(outboundOrders, outboundOrderItems),
                pageData.getTotalCount());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void outbound(OutboundRequest outboundRequest, AuthUser currentUser) {
        OutboundOrder outboundOrder = exactlyFindByCode(outboundRequest.getOutboundOrderCode());
        BusinessException.assertTrue(OutboundOrderStatus.WAIT_OUTBOUND.equals(outboundOrder.getStatus()),
                "出库单已出库");

        outboundOrder.setStatus(OutboundOrderStatus.OUTBOUND_FINISHED);
        outboundOrder.setUpdateUser(currentUser.getCode(), currentUser.getNickName());

        outboundOrderMapper.updateById(outboundOrder);
        inventoryOutbound(outboundOrder, currentUser);
    }

    private void inventoryOutbound(OutboundOrder outboundOrder, AuthUser operator) {
        List<InventoryChangeCommand.Item> outboundItems = outboundOrder.getItems()
                .stream()
                .map(e -> new InventoryChangeCommand.Item(e.getSkuCode(), e.getQuantity()))
                .collect(Collectors.toList());

        String serialCode = codeGenerator.dailyNext(PharmacyBusinessIdConstants.INVENTORY_OUTBOUND);

        InventoryOutboundCommand outboundCommand = new InventoryOutboundCommand(
                outboundOrder.getCode(),
                serialCode,
                outboundOrder.getClinicCode(),
                outboundItems,
                operator.getNickName(),
                operator.getCode(),
                outboundOrder.getSourceBusinessCode());
        inventoryService.outbound(outboundCommand);
    }

    private void save(OutboundOrder outboundOrder) {
        if (outboundOrder.getId() == null) {
            outboundOrderMapper.insert(outboundOrder);
            outboundOrderItemMapper.batchInsert(outboundOrder.getItems());
        } else {
            outboundOrderMapper.updateById(outboundOrder);
            for (OutboundOrderItem item : outboundOrder.getItems()) {
                outboundOrderItemMapper.updateById(item);
            }
        }
    }

    private OutboundOrder exactlyFindByCode(String code) {
        OutboundOrder outboundOrder = outboundOrderMapper.findByCode(code);
        DomainNotFoundException.assertFound(outboundOrder, code);

        outboundOrder.setItems(outboundOrderItemMapper.listByOutboundOrderCode(code));
        return outboundOrder;
    }

}
