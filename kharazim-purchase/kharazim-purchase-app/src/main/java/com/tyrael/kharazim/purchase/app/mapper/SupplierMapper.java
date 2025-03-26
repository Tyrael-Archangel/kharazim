package com.tyrael.kharazim.purchase.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import com.tyrael.kharazim.mybatis.LambdaQueryWrapperX;
import com.tyrael.kharazim.purchase.app.domain.SupplierDO;
import com.tyrael.kharazim.purchase.app.vo.supplier.ListSupplierRequest;
import com.tyrael.kharazim.purchase.app.vo.supplier.PageSupplierRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author Tyrael Archangel
 * @since 2024/2/22
 */
@Mapper
public interface SupplierMapper extends BasePageMapper<SupplierDO> {

    /**
     * find by code
     *
     * @param code code
     * @return SupplierDO
     */
    default SupplierDO findByCode(String code) {
        LambdaQueryWrapper<SupplierDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SupplierDO::getCode, code);
        return selectOne(queryWrapper);
    }

    /**
     * 供应商分页查询
     *
     * @param pageRequest {@link PageSupplierRequest}
     * @return 供应商分页数据
     */
    default PageResponse<SupplierDO> page(PageSupplierRequest pageRequest) {
        LambdaQueryWrapperX<SupplierDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.likeIfPresent(SupplierDO::getName, pageRequest.getName());
        queryWrapper.orderByAsc(SupplierDO::getCode);
        return page(pageRequest, queryWrapper);
    }

    /**
     * 供应商列表查询
     *
     * @param listRequest {@link ListSupplierRequest}
     * @return 供应商列表数据
     */
    default List<SupplierDO> list(ListSupplierRequest listRequest) {
        LambdaQueryWrapperX<SupplierDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.likeIfPresent(SupplierDO::getName, listRequest.getName());
        queryWrapper.orderByAsc(SupplierDO::getCode);
        return selectList(queryWrapper);
    }

    /**
     * list by code
     *
     * @param codes codes
     * @return Suppliers
     */
    default List<SupplierDO> listByCodes(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SupplierDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(SupplierDO::getCode, codes);
        return selectList(queryWrapper);
    }

    /**
     * map by codes
     *
     * @param codes codes
     * @return code -> SupplierDO
     */
    default Map<String, SupplierDO> mapByCodes(Collection<String> codes) {
        List<SupplierDO> suppliers = this.listByCodes(codes);
        return suppliers.stream()
                .collect(Collectors.toMap(SupplierDO::getCode, e -> e));
    }

    /**
     * 验证供应商存在
     *
     * @param code 供应商编码
     */
    default void ensureSupplierExist(String code) {
        LambdaQueryWrapper<SupplierDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SupplierDO::getCode, code);
        if (!this.exists(queryWrapper)) {
            throw new DomainNotFoundException("supplier code: " + code);
        }
    }

}
