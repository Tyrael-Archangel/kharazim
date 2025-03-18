package com.tyrael.kharazim.basicdata.app.service.supplier;


import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.basicdata.app.dto.supplier.AddSupplierRequest;
import com.tyrael.kharazim.basicdata.app.dto.supplier.ListSupplierRequest;
import com.tyrael.kharazim.basicdata.app.dto.supplier.PageSupplierRequest;
import com.tyrael.kharazim.basicdata.app.dto.supplier.SupplierDTO;

import java.util.Collection;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/2/22
 */
public interface SupplierService {

    /**
     * list by codes
     *
     * @param codes codes
     * @return suppliers
     */
    List<SupplierDTO> listByCodes(Collection<String> codes);

    /**
     * find by code
     *
     * @param code code
     * @return supplier
     */
    SupplierDTO findByCode(String code);

    /**
     * 供应商分页查询
     *
     * @param pageRequest {@link PageSupplierRequest}
     * @return 供应商分页数据
     */
    PageResponse<SupplierDTO> page(PageSupplierRequest pageRequest);

    /**
     * 供应商列表数据
     *
     * @param listRequest {@link ListSupplierRequest}
     * @return 供应商列表数据
     */
    List<SupplierDTO> list(ListSupplierRequest listRequest);

    /**
     * 新建供应商
     *
     * @param addSupplierRequest {@link AddSupplierRequest}
     * @return 供应商编码
     */
    String add(AddSupplierRequest addSupplierRequest);

}
