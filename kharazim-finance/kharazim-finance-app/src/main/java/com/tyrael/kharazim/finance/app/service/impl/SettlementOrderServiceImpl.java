package com.tyrael.kharazim.finance.app.service.impl;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.finance.app.constant.FinanceBusinessIdConstants;
import com.tyrael.kharazim.finance.app.converter.SettlementOrderConverter;
import com.tyrael.kharazim.finance.app.domain.SettlementOrder;
import com.tyrael.kharazim.finance.app.domain.SettlementOrderItem;
import com.tyrael.kharazim.finance.app.enums.SettlementOrderStatus;
import com.tyrael.kharazim.finance.app.mapper.SettlementOrderItemMapper;
import com.tyrael.kharazim.finance.app.mapper.SettlementOrderMapper;
import com.tyrael.kharazim.finance.app.service.SettlementOrderService;
import com.tyrael.kharazim.finance.app.vo.settlement.PageSettlementOrderRequest;
import com.tyrael.kharazim.finance.app.vo.settlement.SettlementOrderVO;
import com.tyrael.kharazim.finance.sdk.model.message.CreatePrescriptionSettlementMessage;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
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
    private final IdGenerator codeGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFromPrescription(CreatePrescriptionSettlementMessage message) {

        SettlementOrder settlementOrder = new SettlementOrder();
        settlementOrder.setCode(codeGenerator.dailyNext(FinanceBusinessIdConstants.SETTLEMENT_ORDER));
        settlementOrder.setCustomerCode(message.getCustomerCode());
        settlementOrder.setClinicCode(message.getClinicCode());
        settlementOrder.setTotalAmount(message.getTotalAmount());
        settlementOrder.setSourcePrescriptionCode(message.getPrescriptionCode());
        settlementOrder.setStatus(SettlementOrderStatus.UNPAID);

        List<SettlementOrderItem> items = message.getItems()
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
                pageData.getTotalCount());
    }

}
