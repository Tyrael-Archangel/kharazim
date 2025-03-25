package com.tyrael.kharazim.purchase.sdk.service;

import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.purchase.sdk.model.SupplierVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Tyrael Archangel
 * @since 2025/3/18
 */
public interface SupplierServiceApi {

    /**
     * find supplier by code
     *
     * @param code supplierCode
     * @return Supplier
     */
    SupplierVO findByCode(String code);

    /**
     * map by codes
     *
     * @param codes codes
     * @return code -> SupplierVO
     */
    Map<String, SupplierVO> mapByCodes(Collection<String> codes);

    /**
     * list all supplier
     *
     * @return suppliers
     */
    List<SupplierVO> listAll();

    /**
     * ensure supplier code exist
     *
     * @param code supplierCode
     * @throws DomainNotFoundException DomainNotFoundException
     */
    default void ensureSupplierExist(String code) throws DomainNotFoundException {
        DomainNotFoundException.assertFound(findByCode(code), code);
    }

}
