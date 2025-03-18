package com.tyrael.kharazim.basicdata.sdk.service;

import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.basicdata.sdk.model.SupplierVO;

import java.util.Collection;
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
     * ensure supplier code exist
     *
     * @param code supplierCode
     * @throws DomainNotFoundException DomainNotFoundException
     */
    default void ensureSupplierExist(String code) throws DomainNotFoundException {
        DomainNotFoundException.assertFound(findByCode(code), code);
    }

}
