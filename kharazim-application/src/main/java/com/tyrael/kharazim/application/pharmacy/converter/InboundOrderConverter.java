package com.tyrael.kharazim.application.pharmacy.converter;

import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.pharmacy.domain.InboundOrder;
import com.tyrael.kharazim.application.pharmacy.domain.InboundOrderItem;
import com.tyrael.kharazim.application.pharmacy.vo.inboundorder.InboundOrderVO;
import com.tyrael.kharazim.application.product.service.ProductSkuRepository;
import com.tyrael.kharazim.application.product.vo.sku.ProductSkuVO;
import com.tyrael.kharazim.application.supplier.domain.SupplierDO;
import com.tyrael.kharazim.application.supplier.mapper.SupplierMapper;
import com.tyrael.kharazim.common.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
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

    private final ProductSkuRepository skuRepository;
    private final ClinicMapper clinicMapper;
    private final SupplierMapper supplierMapper;

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

        Map<String, Clinic> clinicMap = clinicMapper.mapByCodes(clinicCodes);
        Map<String, SupplierDO> supplierMap = supplierMapper.mapByCodes(supplierCodes);
        Map<String, ProductSkuVO> skuMap = skuRepository.mapByCodes(skuCodes);

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
                                          Clinic clinic,
                                          SupplierDO supplier,
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
                .defaultImageUrl(skuVO.getDefaultImageUrl())
                .quantity(inboundOrderItem.getQuantity())
                .inboundedQuantity(inboundOrderItem.getInboundedQuantity())
                .remainQuantity(inboundOrderItem.getRemainQuantity())
                .build();
    }

}
