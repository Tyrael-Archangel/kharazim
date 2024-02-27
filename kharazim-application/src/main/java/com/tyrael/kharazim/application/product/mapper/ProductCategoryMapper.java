package com.tyrael.kharazim.application.product.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.product.domain.ProductCategoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/2/27
 */
@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategoryDO> {

    /**
     * list all
     *
     * @return all categories
     */
    default List<ProductCategoryDO> listAll() {
        LambdaQueryWrapper<ProductCategoryDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByAsc(ProductCategoryDO::getCode);
        return selectList(queryWrapper);
    }

}
