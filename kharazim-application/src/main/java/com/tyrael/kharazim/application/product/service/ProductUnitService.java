package com.tyrael.kharazim.application.product.service;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.product.vo.*;
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

    /**
     * 修改商品单位
     *
     * @param modifyUnitRequest {@link ModifyProductUnitRequest}
     * @param currentUser       操作人
     */
    void modify(ModifyProductUnitRequest modifyUnitRequest, AuthUser currentUser);

}
