package com.tyrael.kharazim.product.app.service.impl;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.basicdata.sdk.service.ClinicServiceApi;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
import com.tyrael.kharazim.product.app.constant.ProductBusinessIdConstants;
import com.tyrael.kharazim.product.app.converter.SkuPublishConverter;
import com.tyrael.kharazim.product.app.domain.ProductSku;
import com.tyrael.kharazim.product.app.domain.SkuPublish;
import com.tyrael.kharazim.product.app.mapper.ProductSkuMapper;
import com.tyrael.kharazim.product.app.mapper.SkuPublishMapper;
import com.tyrael.kharazim.product.app.service.SkuPublishService;
import com.tyrael.kharazim.product.app.vo.skupublish.PageSkuPublishRequest;
import com.tyrael.kharazim.product.app.vo.skupublish.PublishSkuRequest;
import com.tyrael.kharazim.product.app.vo.skupublish.SkuPublishVO;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/3/26
 */
@Service
@RequiredArgsConstructor
public class SkuPublishServiceImpl implements SkuPublishService {

    private final SkuPublishMapper skuPublishMapper;
    private final ProductSkuMapper productSkuMapper;
    private final SkuPublishConverter skuPublishConverter;
    private final IdGenerator idGenerator;

    @DubboReference
    private ClinicServiceApi clinicServiceApi;

    @Override
    public PageResponse<SkuPublishVO> page(PageSkuPublishRequest pageRequest) {
        PageResponse<SkuPublish> pageData = skuPublishMapper.page(pageRequest);
        return PageResponse.success(
                skuPublishConverter.skuPublishVOs(pageData.getData()),
                pageData.getTotalCount());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String publish(PublishSkuRequest publishRequest) {

        String skuCode = publishRequest.getSkuCode();
        String clinicCode = publishRequest.getClinicCode();
        LocalDateTime effectBegin = publishRequest.getEffectBegin();
        LocalDateTime effectEnd = publishRequest.getEffectEnd();

        BusinessException.assertTrue(effectEnd.isAfter(effectBegin), "生效时间必须小于失效时间");

        synchronized (("business_publish_sku__" + skuCode + "___" + clinicCode).intern()) {
            boolean exists = skuPublishMapper.publishExists(skuCode, clinicCode, effectBegin, effectEnd);
            BusinessException.assertTrue(!exists, "该时间范围内已存在发布数据");

            ProductSku productSku = productSkuMapper.getByCode(skuCode);
            DomainNotFoundException.assertFound(productSku, skuCode);
            clinicServiceApi.exactlyFindByCode(clinicCode);

            SkuPublish skuPublish = new SkuPublish();
            skuPublish.setCode(idGenerator.dailyTimeNext(ProductBusinessIdConstants.SKU_PUBLISH));
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
        skuPublish.setUpdateUser(currentUser.getCode(), currentUser.getNickName());

        skuPublishMapper.saveCanceled(skuPublish);
    }

    @Override
    public List<SkuPublishVO> listClinicEffective(String clinicCode, Collection<String> skuCodes) {
        List<SkuPublish> skuPublishes = skuPublishMapper.listClinicEffective(clinicCode, skuCodes);
        return skuPublishConverter.skuPublishVOs(skuPublishes);
    }

}
