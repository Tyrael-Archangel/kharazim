package com.tyrael.kharazim.application.product.service;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.product.domain.ProductCategory;
import com.tyrael.kharazim.application.product.vo.category.AddProductCategoryRequest;
import com.tyrael.kharazim.application.product.vo.category.ModifyProductCategoryRequest;
import com.tyrael.kharazim.application.product.vo.category.ProductCategoryTreeNodeDTO;
import com.tyrael.kharazim.application.product.vo.category.ProductCategoryVO;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;

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

    /**
     * 所有商品分类
     *
     * @return 所有商品分类
     */
    List<ProductCategoryVO> all();

    /**
     * 根据编码查询商品分类
     *
     * @param code 商品分类编码
     * @return 商品分类
     */
    default ProductCategoryVO find(String code) {
        return all().stream()
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DomainNotFoundException(code));
    }

    /**
     * 新建商品分类
     *
     * @param addRequest {@link AddProductCategoryRequest}
     * @return 分类编码
     */
    String add(AddProductCategoryRequest addRequest);

    /**
     * get by code
     *
     * @param code 商品分类编码
     * @return 商品分类
     */
    ProductCategory getByCode(String code);

    /**
     * 修改商品分类
     *
     * @param modifyRequest {@link ModifyProductCategoryRequest}
     * @param currentUser   操作人
     */
    void modify(ModifyProductCategoryRequest modifyRequest, AuthUser currentUser);

}
