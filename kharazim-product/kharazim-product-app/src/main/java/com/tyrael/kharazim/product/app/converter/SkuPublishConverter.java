package com.tyrael.kharazim.product.app.converter;

import com.tyrael.kharazim.basicdata.sdk.model.ClinicVO;
import com.tyrael.kharazim.basicdata.sdk.service.ClinicServiceApi;
import com.tyrael.kharazim.product.app.domain.SkuPublish;
import com.tyrael.kharazim.product.app.service.ProductSkuRepository;
import com.tyrael.kharazim.product.app.vo.sku.ProductSkuDTO;
import com.tyrael.kharazim.product.app.vo.skupublish.SkuPublishVO;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
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

    @DubboReference
    private ClinicServiceApi clinicServiceApi;

    /**
     * SkuPublishes -> SkuPublishVOs
     */
    public List<SkuPublishVO> skuPublishVOs(Collection<SkuPublish> skuPublishes) {
        if (skuPublishes == null || skuPublishes.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> skuCodes = new HashSet<>();
        Set<String> clinicCodes = new HashSet<>();
        for (SkuPublish skuPublish : skuPublishes) {
            skuCodes.add(skuPublish.getSkuCode());
            clinicCodes.add(skuPublish.getClinicCode());
        }

        Map<String, ProductSkuDTO> skuMap = productSkuRepository.mapByCodes(skuCodes);
        Map<String, ClinicVO> clinicMap = clinicServiceApi.mapByCodes(clinicCodes);

        return skuPublishes.stream()
                .map(skuPublish -> this.skuPublishVO(skuPublish,
                        skuMap.get(skuPublish.getSkuCode()),
                        clinicMap.get(skuPublish.getClinicCode())))
                .collect(Collectors.toList());
    }

    public SkuPublishVO skuPublishVO(SkuPublish skuPublish,
                                     ProductSkuDTO sku,
                                     ClinicVO clinic) {
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