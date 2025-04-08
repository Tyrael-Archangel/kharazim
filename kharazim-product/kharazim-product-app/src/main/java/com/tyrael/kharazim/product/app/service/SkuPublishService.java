package com.tyrael.kharazim.product.app.service;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.product.app.vo.skupublish.PageSkuPublishRequest;
import com.tyrael.kharazim.product.app.vo.skupublish.PublishSkuRequest;
import com.tyrael.kharazim.product.app.vo.skupublish.SkuPublishVO;
import com.tyrael.kharazim.user.sdk.model.AuthUser;

import java.util.Collection;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/3/16
 */
public interface SkuPublishService {

    /**
     * 商品发布数据分页
     *
     * @param pageRequest {@link PageSkuPublishRequest}
     * @return 商品发布数据分页
     */
    PageResponse<SkuPublishVO> page(PageSkuPublishRequest pageRequest);

    /**
     * 发布商品
     *
     * @param publishRequest {@link PublishSkuRequest}
     * @return 商品发布序列号
     */
    String publish(PublishSkuRequest publishRequest);

    /**
     * 取消发布
     *
     * @param code        商品发布序列号
     * @param currentUser 操作人
     */
    void cancelPublish(String code, AuthUser currentUser);

    /**
     * list clinic effective skuPublishes
     *
     * @param clinicCode 诊所
     * @param skuCodes   sku
     * @return 商品发布数据
     */
    List<SkuPublishVO> listClinicEffective(String clinicCode, Collection<String> skuCodes);

}
