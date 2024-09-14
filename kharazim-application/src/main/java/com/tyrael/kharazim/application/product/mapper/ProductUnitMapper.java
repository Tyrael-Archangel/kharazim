package com.tyrael.kharazim.application.product.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.base.BasePageMapper;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.product.domain.ProductUnitDO;
import com.tyrael.kharazim.application.product.vo.unit.ListProductUnitRequest;
import com.tyrael.kharazim.application.product.vo.unit.PageProductUnitRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import com.tyrael.kharazim.common.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/2/20
 */
@Mapper
public interface ProductUnitMapper extends BasePageMapper<ProductUnitDO> {

    /**
     * 商品单位分页查询
     *
     * @param pageRequest {@link PageProductUnitRequest}
     * @return 商品单位分页数据
     */
    default PageResponse<ProductUnitDO> page(PageProductUnitRequest pageRequest) {
        LambdaQueryWrapperX<ProductUnitDO> queryWrapper = new LambdaQueryWrapperX<>();
        String name = pageRequest.getName();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.and(q -> q.like(ProductUnitDO::getName, name)
                    .or()
                    .like(ProductUnitDO::getEnglishName, name));
        }
        queryWrapper.orderByAsc(ProductUnitDO::getCode);

        return page(pageRequest, queryWrapper);
    }

    /**
     * 商品单位列表数据
     *
     * @param listRequest {@link ListProductUnitRequest}
     * @return 商品单位列表数据
     */
    default List<ProductUnitDO> list(ListProductUnitRequest listRequest) {
        LambdaQueryWrapper<ProductUnitDO> queryWrapper = new LambdaQueryWrapper<>();
        String name = listRequest.getName();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.and(q -> q.like(ProductUnitDO::getName, name)
                    .or()
                    .like(ProductUnitDO::getEnglishName, name));
        }
        queryWrapper.orderByAsc(ProductUnitDO::getCode);
        return selectList(queryWrapper);
    }


    /**
     * find by code
     *
     * @param unitCode 商品单位编码
     * @return ProductUnitDO
     */
    default ProductUnitDO findByCode(String unitCode) {
        LambdaQueryWrapper<ProductUnitDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProductUnitDO::getCode, unitCode);
        return selectOne(queryWrapper);
    }

    /**
     * 验证单位存在
     *
     * @param code 单位编码
     */
    default void ensureUnitExist(String code) {
        LambdaQueryWrapper<ProductUnitDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProductUnitDO::getCode, code);
        if (!this.exists(queryWrapper)) {
            throw new DomainNotFoundException("supplier code: " + code);
        }
    }

    /**
     * list by code
     *
     * @param codes codes
     * @return ProductUnits
     */
    default List<ProductUnitDO> listByCodes(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ProductUnitDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(ProductUnitDO::getCode, codes);
        return selectList(queryWrapper);
    }

    /**
     * map by codes
     *
     * @param codes codes
     * @return code -> ProductUnitDO
     */
    default Map<String, ProductUnitDO> mapByCodes(Collection<String> codes) {
        List<ProductUnitDO> units = this.listByCodes(codes);
        return units.stream()
                .collect(Collectors.toMap(ProductUnitDO::getCode, e -> e));
    }

}
