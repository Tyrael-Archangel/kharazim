package com.tyrael.kharazim.finance.app.converter;

import com.tyrael.kharazim.basicdata.sdk.model.ClinicVO;
import com.tyrael.kharazim.basicdata.sdk.model.CustomerVO;
import com.tyrael.kharazim.basicdata.sdk.service.ClinicServiceApi;
import com.tyrael.kharazim.basicdata.sdk.service.CustomerServiceApi;
import com.tyrael.kharazim.finance.app.domain.SettlementOrder;
import com.tyrael.kharazim.finance.app.domain.SettlementOrderItem;
import com.tyrael.kharazim.finance.app.vo.settlement.SettlementOrderVO;
import com.tyrael.kharazim.product.sdk.model.ProductSkuVO;
import com.tyrael.kharazim.product.sdk.service.ProductServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
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

    @DubboReference
    private ProductServiceApi productServiceApi;
    @DubboReference
    private CustomerServiceApi customerServiceApi;
    @DubboReference
    private ClinicServiceApi clinicServiceApi;

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

        Set<String> customerCodes = new HashSet<>();
        Set<String> clinicCodes = new HashSet<>();
        for (SettlementOrder prescription : settlementOrders) {
            customerCodes.add(prescription.getCustomerCode());
            clinicCodes.add(prescription.getClinicCode());
        }
        Map<String, CustomerVO> customerMap = customerServiceApi.mapByCodes(customerCodes);
        Map<String, ClinicVO> clinicMap = clinicServiceApi.mapByCodes(clinicCodes);

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
            skuMap = productServiceApi.mapByCodes(skuCodes);
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
                                                CustomerVO customer,
                                                ClinicVO clinic,
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
