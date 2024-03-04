package com.tyrael.kharazim.application.supplier.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.supplier.domain.SupplierDO;
import com.tyrael.kharazim.application.supplier.vo.ListSupplierRequest;
import com.tyrael.kharazim.application.supplier.vo.PageSupplierRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.CollectionUtils;

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
public interface SupplierMapper extends BaseMapper<SupplierDO> {

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
        Page<SupplierDO> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<SupplierDO> pageResponse = selectPage(page, queryWrapper);
        return PageResponse.success(pageResponse.getRecords(),
                pageResponse.getTotal(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
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
}
