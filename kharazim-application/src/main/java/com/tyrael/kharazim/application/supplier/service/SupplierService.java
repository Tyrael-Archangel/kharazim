package com.tyrael.kharazim.application.supplier.service;

import com.tyrael.kharazim.application.supplier.vo.PageSupplierRequest;
import com.tyrael.kharazim.application.supplier.vo.SupplierVO;
import com.tyrael.kharazim.common.dto.PageResponse;

/**
 * @author Tyrael Archangel
 * @since 2024/2/22
 */
public interface SupplierService {

    /**
     * 供应商分页查询
     *
     * @param pageRequest {@link PageSupplierRequest}
     * @return 供应商分页数据
     */
    PageResponse<SupplierVO> page(PageSupplierRequest pageRequest);

}
