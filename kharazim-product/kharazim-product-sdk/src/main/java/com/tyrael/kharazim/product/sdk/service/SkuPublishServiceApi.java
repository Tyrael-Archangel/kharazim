package com.tyrael.kharazim.product.sdk.service;

import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.product.sdk.model.SkuPublishedVO;

import java.util.Collection;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/4/7
 */
public interface SkuPublishServiceApi {

    /**
     * 查询诊所有效的商品发布信息
     *
     * @param clinicCode 诊所编码
     * @param skuCodes   SKU编码
     * @return 诊所有效的商品发布信息
     */
    List<SkuPublishedVO> listClinicEffective(String clinicCode, Collection<String> skuCodes);

    /**
     * page effective
     *
     * @param pageCommand PageCommand
     * @return SkuPublishedVOs
     */
    PageResponse<SkuPublishedVO> pageEffective(PageCommand pageCommand);

}
