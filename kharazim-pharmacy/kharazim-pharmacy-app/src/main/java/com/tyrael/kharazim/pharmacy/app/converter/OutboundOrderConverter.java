package com.tyrael.kharazim.pharmacy.app.converter;

import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.sdk.model.ClinicVO;
import com.tyrael.kharazim.basicdata.sdk.model.CustomerVO;
import com.tyrael.kharazim.basicdata.sdk.service.ClinicServiceApi;
import com.tyrael.kharazim.basicdata.sdk.service.CustomerServiceApi;
import com.tyrael.kharazim.pharmacy.app.domain.OutboundOrder;
import com.tyrael.kharazim.pharmacy.app.domain.OutboundOrderItem;
import com.tyrael.kharazim.pharmacy.app.vo.outbound.OutboundOrderVO;
import com.tyrael.kharazim.product.sdk.model.ProductSkuVO;
import com.tyrael.kharazim.product.sdk.service.ProductServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
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

    @DubboReference
    private ClinicServiceApi clinicServiceApi;
    @DubboReference
    private CustomerServiceApi customerServiceApi;
    @DubboReference
    private ProductServiceApi productServiceApi;

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
        Map<String, ClinicVO> clinicMap = clinicServiceApi.mapByCodes(clinicCodes);
        Map<String, CustomerVO> customerMap = customerServiceApi.mapByCodes(customerCodes);

        Set<String> skuCodes = CollectionUtils.safeStream(outboundOrderItems)
                .map(OutboundOrderItem::getSkuCode)
                .collect(Collectors.toSet());
        Map<String, ProductSkuVO> skuMap = productServiceApi.mapByCodes(skuCodes);
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
                                            ClinicVO clinic,
                                            CustomerVO customer,
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
                .defaultImage(skuVO.getDefaultImage())
                .quantity(outboundOrderItem.getQuantity())
                .build();
    }
}
