package com.tyrael.kharazim.basicdata.app.mapper.supplier;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.app.domain.supplier.SupplierDO;
import com.tyrael.kharazim.basicdata.app.dto.supplier.ListSupplierRequest;
import com.tyrael.kharazim.basicdata.app.dto.supplier.PageSupplierRequest;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import com.tyrael.kharazim.mybatis.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


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
