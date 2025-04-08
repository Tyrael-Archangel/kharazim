package com.tyrael.kharazim.product.provider;

import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.product.app.enums.SkuPublishStatus;
import com.tyrael.kharazim.product.app.service.SkuPublishService;
import com.tyrael.kharazim.product.app.vo.skupublish.PageSkuPublishRequest;
import com.tyrael.kharazim.product.app.vo.skupublish.SkuPublishVO;
import com.tyrael.kharazim.product.sdk.model.SkuPublishedVO;
import com.tyrael.kharazim.product.sdk.service.SkuPublishServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/4/8
 */
@Component
@DubboService
@RequiredArgsConstructor
public class SkuPublishServiceProvider implements SkuPublishServiceApi {

    private final SkuPublishService skuPublishService;

    @Override
    public List<SkuPublishedVO> listClinicEffective(String clinicCode, Collection<String> skuCodes) {
        return this.convert(skuPublishService.listClinicEffective(clinicCode, skuCodes));
    }

    @Override
    public PageResponse<SkuPublishedVO> pageEffective(PageCommand pageCommand) {
        PageSkuPublishRequest pageRequest = new PageSkuPublishRequest();
        pageRequest.setPublishStatus(SkuPublishStatus.IN_EFFECT);
        pageRequest.setPageIndex(pageCommand.getPageIndex());
        pageRequest.setPageSize(pageCommand.getPageSize());
        PageResponse<SkuPublishVO> pageData = skuPublishService.page(pageRequest);
        return PageResponse.success(this.convert(pageData.getData()), pageData.getTotalCount());
    }

    private List<SkuPublishedVO> convert(Collection<SkuPublishVO> publishes) {
        return CollectionUtils.safeStream(publishes)
                .map(e -> new SkuPublishedVO()
                        .setClinicCode(e.getClinicCode())
                        .setSkuCode(e.getSkuCode())
                        .setPrice(e.getPrice()))
                .collect(Collectors.toList());
    }

}
