package com.tyrael.kharazim.application.settlement.service.impl;

import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.prescription.domain.Prescription;
import com.tyrael.kharazim.application.settlement.converter.SettlementOrderConverter;
import com.tyrael.kharazim.application.settlement.domain.SettlementOrder;
import com.tyrael.kharazim.application.settlement.domain.SettlementOrderItem;
import com.tyrael.kharazim.application.settlement.enums.SettlementOrderStatus;
import com.tyrael.kharazim.application.settlement.mapper.SettlementOrderItemMapper;
import com.tyrael.kharazim.application.settlement.mapper.SettlementOrderMapper;
import com.tyrael.kharazim.application.settlement.service.SettlementOrderService;
import com.tyrael.kharazim.application.settlement.vo.PageSettlementOrderRequest;
import com.tyrael.kharazim.application.settlement.vo.SettlementOrderVO;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Service
@RequiredArgsConstructor
public class SettlementOrderServiceImpl implements SettlementOrderService {

    private final SettlementOrderMapper settlementOrderMapper;
    private final SettlementOrderItemMapper settlementOrderItemMapper;
    private final SettlementOrderConverter settlementOrderConverter;
    private final CodeGenerator codeGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFromPrescription(Prescription prescription) {

        SettlementOrder settlementOrder = new SettlementOrder();
        settlementOrder.setCode(codeGenerator.dailyNext(BusinessCodeConstants.SETTLEMENT_ORDER));
        settlementOrder.setCustomerCode(prescription.getCustomerCode());
        settlementOrder.setClinicCode(prescription.getClinicCode());
        settlementOrder.setTotalAmount(prescription.getTotalAmount());
        settlementOrder.setSourcePrescriptionCode(prescription.getCode());
        settlementOrder.setStatus(SettlementOrderStatus.UNPAID);

        List<SettlementOrderItem> items = prescription.getProducts()
                .stream()
                .map(e -> {
                    SettlementOrderItem settlementOrderItem = new SettlementOrderItem();
                    settlementOrderItem.setSettlementOrderCode(settlementOrder.getCode());
                    settlementOrderItem.setSkuCode(e.getSkuCode());
                    settlementOrderItem.setQuantity(e.getQuantity());
                    settlementOrderItem.setPrice(e.getPrice());
                    settlementOrderItem.setAmount(e.getAmount());
                    return settlementOrderItem;
                })
                .collect(Collectors.toList());
        settlementOrder.setItems(items);

        this.save(settlementOrder);
    }

    private void save(SettlementOrder settlementOrder) {
        settlementOrderMapper.insert(settlementOrder);
        settlementOrderItemMapper.batchInsert(settlementOrder.getItems());
    }

    @Override
    public SettlementOrderVO findByCode(String code) {
        SettlementOrder settlementOrder = settlementOrderMapper.findByCode(code);
        DomainNotFoundException.assertFound(settlementOrder, code);

        List<SettlementOrderItem> settlementOrderItems = settlementOrderItemMapper.listBySettlementOrderCode(code);
        return settlementOrderConverter.settlementOrderVO(settlementOrder, settlementOrderItems);
    }

    @Override
    public PageResponse<SettlementOrderVO> page(PageSettlementOrderRequest pageRequest) {
        PageResponse<SettlementOrder> pageData = settlementOrderMapper.page(pageRequest);
        Collection<SettlementOrder> settlementOrders = pageData.getData();
        List<String> codes = settlementOrders.stream()
                .map(SettlementOrder::getCode)
                .collect(Collectors.toList());
        List<SettlementOrderItem> settlementOrderItems = settlementOrderItemMapper.listBySettlementOrderCodes(codes);

        return PageResponse.success(
                settlementOrderConverter.settlementOrderVOs(settlementOrders, settlementOrderItems),
                pageData.getTotalCount(),
                pageData.getPageSize(),
                pageData.getPageNum());
    }

}
