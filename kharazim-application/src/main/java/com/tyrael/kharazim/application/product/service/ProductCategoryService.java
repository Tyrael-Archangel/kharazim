package com.tyrael.kharazim.application.product.service;

import com.tyrael.kharazim.application.product.vo.category.ProductCategoryTreeNodeDTO;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/2/27
 */
public interface ProductCategoryService {

    /**
     * 商品分类树数据
     *
     * @return 商品分类树数据
     */
    List<ProductCategoryTreeNodeDTO> tree();

}
