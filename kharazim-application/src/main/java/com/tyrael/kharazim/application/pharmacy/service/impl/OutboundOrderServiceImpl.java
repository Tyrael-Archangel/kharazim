package com.tyrael.kharazim.application.pharmacy.service.impl;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.pharmacy.converter.OutboundOrderConverter;
import com.tyrael.kharazim.application.pharmacy.domain.OutboundOrder;
import com.tyrael.kharazim.application.pharmacy.domain.OutboundOrderItem;
import com.tyrael.kharazim.application.pharmacy.enums.OutboundOrderStatus;
import com.tyrael.kharazim.application.pharmacy.mapper.OutboundOrderItemMapper;
import com.tyrael.kharazim.application.pharmacy.mapper.OutboundOrderMapper;
import com.tyrael.kharazim.application.pharmacy.service.InventoryService;
import com.tyrael.kharazim.application.pharmacy.service.OutboundOrderService;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.InventoryChangeCommand;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.InventoryOutboundCommand;
import com.tyrael.kharazim.application.pharmacy.vo.outboundorder.OutboundOrderVO;
import com.tyrael.kharazim.application.pharmacy.vo.outboundorder.OutboundRequest;
import com.tyrael.kharazim.application.pharmacy.vo.outboundorder.PageOutboundOrderRequest;
import com.tyrael.kharazim.application.prescription.domain.Prescription;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
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

    private final CodeGenerator codeGenerator;
    private final OutboundOrderMapper outboundOrderMapper;
    private final OutboundOrderItemMapper outboundOrderItemMapper;
    private final OutboundOrderConverter outboundOrderConverter;
    private final InventoryService inventoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFromPrescription(Prescription prescription) {
        OutboundOrder outboundOrder = new OutboundOrder();

        outboundOrder.setCode(codeGenerator.dailyNext(BusinessCodeConstants.OUTBOUND_ORDER));
        outboundOrder.setStatus(OutboundOrderStatus.WAIT_OUTBOUND);
        outboundOrder.setSourceBusinessCode(prescription.getCode());
        outboundOrder.setCustomerCode(prescription.getCustomerCode());
        outboundOrder.setClinicCode(prescription.getClinicCode());
        outboundOrder.setSourceRemark(prescription.getRemark());

        List<OutboundOrderItem> outboundOrderItems = prescription.getProducts()
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

        String serialCode = codeGenerator.dailyNext(BusinessCodeConstants.INVENTORY_OUTBOUND);

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
