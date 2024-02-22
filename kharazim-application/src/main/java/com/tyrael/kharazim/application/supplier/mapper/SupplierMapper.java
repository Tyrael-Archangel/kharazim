package com.tyrael.kharazim.application.supplier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.supplier.domain.SupplierDO;
import com.tyrael.kharazim.application.supplier.vo.PageSupplierRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author Tyrael Archangel
 * @since 2024/2/22
 */
@Mapper
public interface SupplierMapper extends BaseMapper<SupplierDO> {

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

}
