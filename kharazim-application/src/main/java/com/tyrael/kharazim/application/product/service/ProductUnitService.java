package com.tyrael.kharazim.application.product.service;

import com.tyrael.kharazim.application.product.vo.AddProductUnitRequest;
import com.tyrael.kharazim.application.product.vo.ListProductUnitRequest;
import com.tyrael.kharazim.application.product.vo.PageProductUnitRequest;
import com.tyrael.kharazim.application.product.vo.ProductUnitVO;
import com.tyrael.kharazim.common.dto.PageResponse;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/2/20
 */
public interface ProductUnitService {

    /**
     * 商品单位分页查询
     *
     * @param pageRequest {@link PageProductUnitRequest}
     * @return 商品单位分页数据
     */
    PageResponse<ProductUnitVO> page(PageProductUnitRequest pageRequest);

    /**
     * 商品单位列表数据
     *
     * @param listRequest {@link ListProductUnitRequest}
     * @return 商品单位列表数据
     */
    List<ProductUnitVO> list(ListProductUnitRequest listRequest);

    /**
     * 新建商品单位
     *
     * @param addUnitRequest {@link AddProductUnitRequest}
     * @return 单位编码
     */
    String add(AddProductUnitRequest addUnitRequest);

}
