package com.tyrael.kharazim.application.skupublish.service.impl;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.product.domain.ProductSku;
import com.tyrael.kharazim.application.product.mapper.ProductSkuMapper;
import com.tyrael.kharazim.application.skupublish.converter.SkuPublishConverter;
import com.tyrael.kharazim.application.skupublish.domain.SkuPublish;
import com.tyrael.kharazim.application.skupublish.mapper.SkuPublishMapper;
import com.tyrael.kharazim.application.skupublish.service.SkuPublishService;
import com.tyrael.kharazim.application.skupublish.vo.PageSkuPublishRequest;
import com.tyrael.kharazim.application.skupublish.vo.PublishSkuRequest;
import com.tyrael.kharazim.application.skupublish.vo.SkuPublishVO;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/3/26
 */
@Service
@RequiredArgsConstructor
public class SkuPublishServiceImpl implements SkuPublishService {

    private final SkuPublishMapper skuPublishMapper;
    private final ProductSkuMapper productSkuMapper;
    private final ClinicMapper clinicMapper;
    private final SkuPublishConverter skuPublishConverter;
    private final CodeGenerator codeGenerator;

    @Override
    public PageResponse<SkuPublishVO> page(PageSkuPublishRequest pageRequest) {
        PageResponse<SkuPublish> pageData = skuPublishMapper.page(pageRequest);
        return PageResponse.success(skuPublishConverter.skuPublishVOs(pageData.getData()),
                pageData.getTotalCount(),
                pageData.getPageSize(),
                pageData.getPageNum());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String publish(PublishSkuRequest publishRequest) {

        String skuCode = publishRequest.getSkuCode();
        String clinicCode = publishRequest.getClinicCode();
        LocalDateTime effectBegin = publishRequest.getEffectBegin();
        LocalDateTime effectEnd = publishRequest.getEffectEnd();

        BusinessException.assertTrue(effectEnd.isAfter(effectBegin), "生效时间必须小于失效时间");

        synchronized ((skuCode + "__" + clinicCode).intern()) {
            boolean exists = skuPublishMapper.publishExists(skuCode, clinicCode, effectBegin, effectEnd);
            BusinessException.assertTrue(!exists, "该时间范围内已存在发布数据");

            ProductSku productSku = productSkuMapper.getByCode(skuCode);
            DomainNotFoundException.assertFound(productSku, skuCode);
            clinicMapper.exactlyFindByCode(clinicCode);

            SkuPublish skuPublish = new SkuPublish();
            skuPublish.setCode(codeGenerator.dailyTimeNext(BusinessCodeConstants.SKU_PUBLISH));
            skuPublish.setCanceled(false);
            skuPublish.setSkuCode(skuCode);
            skuPublish.setClinicCode(clinicCode);
            skuPublish.setPrice(publishRequest.getPrice());
            skuPublish.setEffectBegin(effectBegin);
            skuPublish.setEffectEnd(effectEnd);

            skuPublishMapper.insert(skuPublish);

            return skuPublish.getCode();
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPublish(String code, AuthUser currentUser) {
        SkuPublish skuPublish = skuPublishMapper.findByCode(code);
        DomainNotFoundException.assertFound(skuPublish, code);

        if (Boolean.TRUE.equals(skuPublish.getCanceled())) {
            return;
        }
        skuPublish.setCanceled(Boolean.TRUE);
        skuPublish.setUpdate(currentUser.getCode(), currentUser.getNickName());

        skuPublishMapper.saveCanceled(skuPublish);
    }

}
