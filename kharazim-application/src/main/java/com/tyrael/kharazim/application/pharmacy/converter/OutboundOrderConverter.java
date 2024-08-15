package com.tyrael.kharazim.application.pharmacy.converter;

import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.customer.domain.Customer;
import com.tyrael.kharazim.application.customer.mapper.CustomerMapper;
import com.tyrael.kharazim.application.pharmacy.domain.OutboundOrder;
import com.tyrael.kharazim.application.pharmacy.domain.OutboundOrderItem;
import com.tyrael.kharazim.application.pharmacy.vo.outboundorder.OutboundOrderVO;
import com.tyrael.kharazim.application.product.service.ProductSkuRepository;
import com.tyrael.kharazim.application.product.vo.sku.ProductSkuVO;
import com.tyrael.kharazim.common.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@Component
@RequiredArgsConstructor
public class OutboundOrderConverter {

    private final ClinicMapper clinicMapper;
    private final CustomerMapper customerMapper;
    private final ProductSkuRepository skuRepository;

    /**
     * OutboundOrders、OutboundOrderItems -> OutboundOrderVOs
     */
    public List<OutboundOrderVO> outboundOrderVOs(Collection<OutboundOrder> outboundOrders,
                                                  Collection<OutboundOrderItem> outboundOrderItems) {
        if (CollectionUtils.isEmpty(outboundOrders)) {
            return new ArrayList<>();
        }

        Set<String> clinicCodes = new HashSet<>();
        Set<String> customerCodes = new HashSet<>();
        for (OutboundOrder outboundOrder : outboundOrders) {
            clinicCodes.add(outboundOrder.getClinicCode());
            customerCodes.add(outboundOrder.getCustomerCode());
        }
        Map<String, Clinic> clinicMap = clinicMapper.mapByCodes(clinicCodes);
        Map<String, Customer> customerMap = customerMapper.mapByCodes(customerCodes);

        Set<String> skuCodes = CollectionUtils.safeStream(outboundOrderItems)
                .map(OutboundOrderItem::getSkuCode)
                .collect(Collectors.toSet());
        Map<String, ProductSkuVO> skuMap = skuRepository.mapByCodes(skuCodes);
        Map<String, List<OutboundOrderItem>> outboundItemGroups = CollectionUtils.safeStream(outboundOrderItems)
                .collect(Collectors.groupingBy(OutboundOrderItem::getOutboundOrderCode));

        return outboundOrders.stream()
                .map(e -> outboundOrderVO(e,
                        clinicMap.get(e.getClinicCode()),
                        customerMap.get(e.getCustomerCode()),
                        outboundItemGroups.get(e.getCode()),
                        skuMap))
                .collect(Collectors.toList());
    }

    private OutboundOrderVO outboundOrderVO(OutboundOrder outboundOrder,
                                            Clinic clinic,
                                            Customer customer,
                                            List<OutboundOrderItem> outboundOrderItems,
                                            Map<String, ProductSkuVO> skuMap) {
        List<OutboundOrderVO.Item> items = CollectionUtils.safeStream(outboundOrderItems)
                .map(e -> outboundOrderItem(e, skuMap.get(e.getSkuCode())))
                .collect(Collectors.toList());

        return OutboundOrderVO.builder()
                .code(outboundOrder.getCode())
                .status(outboundOrder.getStatus())
                .sourceBusinessCode(outboundOrder.getSourceBusinessCode())
                .customerCode(customer.getCode())
                .customerName(customer.getName())
                .clinicCode(clinic.getCode())
                .clinicName(clinic.getName())
                .sourceRemark(outboundOrder.getSourceRemark())
                .items(items)
                .build();
    }

    private OutboundOrderVO.Item outboundOrderItem(OutboundOrderItem outboundOrderItem, ProductSkuVO skuVO) {
        return OutboundOrderVO.Item.builder()
                .skuCode(outboundOrderItem.getSkuCode())
                .skuName(skuVO.getName())
                .categoryCode(skuVO.getCategoryCode())
                .categoryName(skuVO.getCategoryName())
                .unitCode(skuVO.getUnitCode())
                .unitName(skuVO.getUnitName())
                .defaultImageUrl(skuVO.getDefaultImageUrl())
                .quantity(outboundOrderItem.getQuantity())
                .build();
    }
}
