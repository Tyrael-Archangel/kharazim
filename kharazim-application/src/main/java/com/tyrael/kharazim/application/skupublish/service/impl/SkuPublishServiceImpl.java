package com.tyrael.kharazim.application.skupublish.service.impl;

import com.tyrael.kharazim.application.skupublish.converter.SkuPublishConverter;
import com.tyrael.kharazim.application.skupublish.domain.SkuPublish;
import com.tyrael.kharazim.application.skupublish.mapper.SkuPublishMapper;
import com.tyrael.kharazim.application.skupublish.service.SkuPublishService;
import com.tyrael.kharazim.application.skupublish.vo.PageSkuPublishRequest;
import com.tyrael.kharazim.application.skupublish.vo.PublishSkuRequest;
import com.tyrael.kharazim.application.skupublish.vo.SkuPublishVO;
import com.tyrael.kharazim.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Tyrael Archangel
 * @since 2024/3/26
 */
@Service
@RequiredArgsConstructor
public class SkuPublishServiceImpl implements SkuPublishService {

    private final SkuPublishMapper skuPublishMapper;
    private final SkuPublishConverter skuPublishConverter;

    @Override
    public PageResponse<SkuPublishVO> pageEffect(PageSkuPublishRequest pageRequest) {
        PageResponse<SkuPublish> pageData = skuPublishMapper.pageEffect(pageRequest);
        return PageResponse.success(skuPublishConverter.skuPublishVOs(pageData.getData()),
                pageData.getTotalCount(),
                pageData.getPageSize(),
                pageData.getPageNum());
    }

    @Override
    public String publish(PublishSkuRequest publishRequest) {
        return null;
    }

    @Override
    public void cancelPublish(String code) {

    }

}
