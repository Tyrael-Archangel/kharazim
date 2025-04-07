package com.tyrael.kharazim.pharmacy.app.converter;

import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.sdk.model.ClinicVO;
import com.tyrael.kharazim.basicdata.sdk.service.ClinicServiceApi;
import com.tyrael.kharazim.pharmacy.app.domain.InboundOrder;
import com.tyrael.kharazim.pharmacy.app.domain.InboundOrderItem;
import com.tyrael.kharazim.pharmacy.app.vo.inbound.InboundOrderVO;
import com.tyrael.kharazim.product.sdk.model.ProductSkuVO;
import com.tyrael.kharazim.product.sdk.service.ProductServiceApi;
import com.tyrael.kharazim.purchase.sdk.model.SupplierVO;
import com.tyrael.kharazim.purchase.sdk.service.SupplierServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/8/13
 */
@Component
@RequiredArgsConstructor
public class InboundOrderConverter {

    @DubboReference
    private ProductServiceApi productServiceApi;
    @DubboReference
    private ClinicServiceApi clinicServiceApi;
    @DubboReference
    private SupplierServiceApi supplierServiceApi;

    public List<InboundOrderVO> inboundOrderVOs(Collection<InboundOrder> inboundOrders,
                                                Collection<InboundOrderItem> inboundOrderItems) {
        if (inboundOrders == null || inboundOrders.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> supplierCodes = new HashSet<>();
        Set<String> clinicCodes = new HashSet<>();
        for (InboundOrder inboundOrder : inboundOrders) {
            supplierCodes.add(inboundOrder.getSupplierCode());
            clinicCodes.add(inboundOrder.getClinicCode());
        }

        Set<String> skuCodes = CollectionUtils.safeStream(inboundOrderItems)
                .map(InboundOrderItem::getSkuCode)
                .collect(Collectors.toSet());

        Map<String, ClinicVO> clinicMap = clinicServiceApi.mapByCodes(clinicCodes);
        Map<String, SupplierVO> supplierMap = supplierServiceApi.mapByCodes(supplierCodes);
        Map<String, ProductSkuVO> skuMap = productServiceApi.mapByCodes(skuCodes);

        Map<String, List<InboundOrderItem>> inboundOrderItemGroups = inboundOrderItems.stream()
                .collect(Collectors.groupingBy(InboundOrderItem::getInboundOrderCode));

        return inboundOrders.stream()
                .map(e -> inboundOrderVO(e,
                        clinicMap.get(e.getClinicCode()),
                        supplierMap.get(e.getSupplierCode()),
                        inboundOrderItemGroups.get(e.getCode()),
                        skuMap))
                .collect(Collectors.toList());
    }

    private InboundOrderVO inboundOrderVO(InboundOrder inboundOrder,
                                          ClinicVO clinic,
                                          SupplierVO supplier,
                                          Collection<InboundOrderItem> inboundOrderItems,
                                          Map<String, ProductSkuVO> skuMap) {

        List<InboundOrderVO.Item> items = CollectionUtils.safeStream(inboundOrderItems)
                .map(item -> inboundOrderItemVO(item, skuMap.get(item.getSkuCode())))
                .collect(Collectors.toList());

        return InboundOrderVO.builder()
                .code(inboundOrder.getCode())
                .sourceBusinessCode(inboundOrder.getSourceBusinessCode())
                .clinicCode(clinic.getCode())
                .clinicName(clinic.getName())
                .supplierCode(supplier.getCode())
                .supplierName(supplier.getName())
                .sourceRemark(inboundOrder.getSourceRemark())
                .sourceType(inboundOrder.getSourceType())
                .status(inboundOrder.getStatus())
                .items(items)
                .build();
    }

    private InboundOrderVO.Item inboundOrderItemVO(InboundOrderItem inboundOrderItem, ProductSkuVO skuVO) {
        return InboundOrderVO.Item.builder()
                .skuCode(inboundOrderItem.getSkuCode())
                .skuName(skuVO.getName())
                .categoryCode(skuVO.getCategoryCode())
                .categoryName(skuVO.getCategoryName())
                .unitCode(skuVO.getUnitCode())
                .unitName(skuVO.getUnitName())
                .defaultImage(skuVO.getDefaultImage())
                .quantity(inboundOrderItem.getQuantity())
                .inboundedQuantity(inboundOrderItem.getInboundedQuantity())
                .remainQuantity(inboundOrderItem.getRemainQuantity())
                .build();
    }

}
