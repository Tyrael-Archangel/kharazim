package com.tyrael.kharazim.application.settlement.converter;

import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.customer.domain.Customer;
import com.tyrael.kharazim.application.customer.mapper.CustomerMapper;
import com.tyrael.kharazim.application.product.service.ProductSkuRepository;
import com.tyrael.kharazim.application.product.vo.sku.ProductSkuVO;
import com.tyrael.kharazim.application.settlement.domain.SettlementOrder;
import com.tyrael.kharazim.application.settlement.domain.SettlementOrderItem;
import com.tyrael.kharazim.application.settlement.vo.SettlementOrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Component
@RequiredArgsConstructor
public class SettlementOrderConverter {

    private final ProductSkuRepository productSkuRepository;
    private final CustomerMapper customerMapper;
    private final ClinicMapper clinicMapper;

    /**
     * SettlementOrder、SettlementOrderItems -> SettlementOrderVO
     */
    public SettlementOrderVO settlementOrderVO(SettlementOrder settlementOrder,
                                               Collection<SettlementOrderItem> settlementOrderItems) {
        if (settlementOrder == null) {
            return null;
        }
        return settlementOrderVOs(Collections.singleton(settlementOrder), settlementOrderItems)
                .iterator().next();
    }

    /**
     * SettlementOrders、SettlementOrderItems -> SettlementOrderVOs
     */
    public List<SettlementOrderVO> settlementOrderVOs(Collection<SettlementOrder> settlementOrders,
                                                      Collection<SettlementOrderItem> settlementOrderItems) {
        if (settlementOrders == null || settlementOrders.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> customerCodes = Sets.newHashSet();
        Set<String> clinicCodes = Sets.newHashSet();
        for (SettlementOrder prescription : settlementOrders) {
            customerCodes.add(prescription.getCustomerCode());
            clinicCodes.add(prescription.getClinicCode());
        }
        Map<String, Customer> customerMap = customerMapper.mapByCodes(customerCodes);
        Map<String, Clinic> clinicMap = clinicMapper.mapByCodes(clinicCodes);

        Map<String, List<SettlementOrderItem>> itemGroups;
        Map<String, ProductSkuVO> skuMap;
        if (settlementOrderItems == null || settlementOrderItems.isEmpty()) {
            itemGroups = Collections.emptyMap();
            skuMap = Collections.emptyMap();
        } else {
            itemGroups = settlementOrderItems.stream()
                    .collect(Collectors.groupingBy(SettlementOrderItem::getSettlementOrderCode));
            List<String> skuCodes = settlementOrderItems.stream()
                    .map(SettlementOrderItem::getSkuCode)
                    .distinct()
                    .toList();
            skuMap = productSkuRepository.mapByCodes(skuCodes);
        }

        return settlementOrders.stream()
                .map(e -> this.settlementOrderVO(e,
                        customerMap.get(e.getCustomerCode()),
                        clinicMap.get(e.getClinicCode()),
                        itemGroups.getOrDefault(e.getCode(), Collections.emptyList()),
                        skuMap))
                .collect(Collectors.toList());
    }

    private SettlementOrderVO settlementOrderVO(SettlementOrder settlementOrder,
                                                Customer customer,
                                                Clinic clinic,
                                                List<SettlementOrderItem> settlementOrderItems,
                                                Map<String, ProductSkuVO> skuMap) {
        SettlementOrderVO settlementOrderVO = new SettlementOrderVO();
        settlementOrderVO.setCode(settlementOrder.getCode());
        settlementOrderVO.setStatus(settlementOrder.getStatus());
        settlementOrderVO.setCustomerCode(customer.getCode());
        settlementOrderVO.setCustomerName(customer.getName());
        settlementOrderVO.setClinicCode(clinic.getCode());
        settlementOrderVO.setClinicName(clinic.getName());
        settlementOrderVO.setSourcePrescriptionCode(settlementOrder.getSourcePrescriptionCode());
        settlementOrderVO.setTotalAmount(settlementOrder.getTotalAmount());
        settlementOrderVO.setCreateTime(settlementOrder.getCreateTime());
        settlementOrderVO.setSettlementTime(settlementOrder.getSettlementTime());

        List<SettlementOrderVO.Item> items = settlementOrderItems.stream()
                .map(e -> this.SettlementOrderItemVO(e, skuMap.get(e.getSkuCode())))
                .collect(Collectors.toList());
        settlementOrderVO.setItems(items);
        return settlementOrderVO;
    }

    private SettlementOrderVO.Item SettlementOrderItemVO(SettlementOrderItem settlementOrderItem,
                                                         ProductSkuVO productInfo) {
        SettlementOrderVO.Item item = new SettlementOrderVO.Item();
        item.setSkuName(productInfo.getName());
        item.setSkuCode(settlementOrderItem.getSkuCode());
        item.setCategoryCode(productInfo.getCategoryCode());
        item.setCategoryName(productInfo.getCategoryName());
        item.setSupplierCode(productInfo.getSupplierCode());
        item.setSupplierName(productInfo.getSupplierName());
        item.setDescription(productInfo.getDescription());
        item.setUnitCode(productInfo.getUnitCode());
        item.setUnitName(productInfo.getUnitName());
        item.setDefaultImage(productInfo.getDefaultImage());
        item.setQuantity(settlementOrderItem.getQuantity());
        item.setPrice(settlementOrderItem.getPrice());
        item.setAmount(settlementOrderItem.getAmount());
        return item;
    }

}
