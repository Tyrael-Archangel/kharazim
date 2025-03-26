package com.tyrael.kharazim.application.skupublish.converter;

import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.product.service.ProductSkuRepository;
import com.tyrael.kharazim.application.product.vo.sku.ProductSkuVO;
import com.tyrael.kharazim.application.skupublish.domain.SkuPublish;
import com.tyrael.kharazim.application.skupublish.vo.SkuPublishVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/3/26
 */
@Component
@RequiredArgsConstructor
public class SkuPublishConverter {

    private final ProductSkuRepository productSkuRepository;
    private final ClinicMapper clinicMapper;

    /**
     * SkuPublishes -> SkuPublishVOs
     */
    public List<SkuPublishVO> skuPublishVOs(Collection<SkuPublish> skuPublishes) {
        if (skuPublishes == null || skuPublishes.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> skuCodes = Sets.newHashSet();
        Set<String> clinicCodes = Sets.newHashSet();
        for (SkuPublish skuPublish : skuPublishes) {
            skuCodes.add(skuPublish.getSkuCode());
            clinicCodes.add(skuPublish.getClinicCode());
        }

        Map<String, ProductSkuVO> skuMap = productSkuRepository.mapByCodes(skuCodes);
        Map<String, Clinic> clinicMap = clinicMapper.mapByCodes(clinicCodes);

        return skuPublishes.stream()
                .map(skuPublish -> this.skuPublishVO(skuPublish,
                        skuMap.get(skuPublish.getSkuCode()),
                        clinicMap.get(skuPublish.getClinicCode())))
                .collect(Collectors.toList());
    }

    public SkuPublishVO skuPublishVO(SkuPublish skuPublish,
                                     ProductSkuVO sku,
                                     Clinic clinic) {
        SkuPublishVO skuPublishVO = new SkuPublishVO();
        skuPublishVO.setCode(skuPublish.getCode());
        skuPublishVO.setPublishStatus(skuPublish.getStatus());
        skuPublishVO.setSkuCode(sku.getCode());
        skuPublishVO.setSkuName(sku.getName());
        skuPublishVO.setClinicCode(clinic.getCode());
        skuPublishVO.setClinicName(clinic.getName());
        skuPublishVO.setCategoryCode(sku.getCategoryCode());
        skuPublishVO.setCategoryName(sku.getCategoryName());
        skuPublishVO.setCategoryFullName(sku.getCategoryFullName());
        skuPublishVO.setSupplierCode(sku.getSupplierCode());
        skuPublishVO.setSupplierName(sku.getSupplierName());
        skuPublishVO.setUnitCode(sku.getUnitCode());
        skuPublishVO.setUnitName(sku.getUnitName());
        skuPublishVO.setPrice(skuPublish.getPrice());
        skuPublishVO.setEffectBegin(skuPublish.getEffectBegin());
        skuPublishVO.setEffectEnd(skuPublish.getEffectEnd());
        skuPublishVO.setDefaultImage(sku.getDefaultImage());
        return skuPublishVO;
    }

}