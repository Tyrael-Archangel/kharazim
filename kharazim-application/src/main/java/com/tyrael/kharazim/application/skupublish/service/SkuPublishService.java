package com.tyrael.kharazim.application.skupublish.service;

import com.tyrael.kharazim.application.skupublish.vo.PageSkuPublishRequest;
import com.tyrael.kharazim.application.skupublish.vo.PublishSkuRequest;
import com.tyrael.kharazim.application.skupublish.vo.SkuPublishVO;
import com.tyrael.kharazim.common.dto.PageResponse;

/**
 * @author Tyrael Archangel
 * @since 2024/3/16
 */
public interface SkuPublishService {

    /**
     * 生效的商品发布数据分页
     *
     * @param pageRequest {@link PageSkuPublishRequest}
     * @return 生效的商品发布数据分页
     */
    PageResponse<SkuPublishVO> pageEffect(PageSkuPublishRequest pageRequest);

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
     * @param code 商品发布序列号
     */
    void cancelPublish(String code);

}
