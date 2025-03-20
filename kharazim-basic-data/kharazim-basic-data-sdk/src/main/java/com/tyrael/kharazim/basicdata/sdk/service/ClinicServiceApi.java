package com.tyrael.kharazim.basicdata.sdk.service;

import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.basicdata.sdk.model.ClinicVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/3/20
 */
public interface ClinicServiceApi {

    /**
     * list all
     *
     * @return all clinics
     */
    List<ClinicVO> listAll();

    /**
     * list by codes
     *
     * @param clinicCodes clinicCodes
     * @return Clinics
     */
    List<ClinicVO> listByCodes(Collection<String> clinicCodes);

    /**
     * map by codes
     *
     * @param clinicCodes clinicCodes
     * @return code -> ClinicVO
     */
    default Map<String, ClinicVO> mapByCodes(Collection<String> clinicCodes) {
        List<ClinicVO> clinics = listByCodes(clinicCodes);
        return clinics.stream()
                .collect(Collectors.toMap(ClinicVO::getCode, e -> e));
    }

    /**
     * find by code
     *
     * @param clinicCode clinicCode
     * @return Clinic
     */
    ClinicVO findByCode(String clinicCode);

    /**
     * exactly find by code
     *
     * @param clinicCode clinicCode
     * @return Clinic
     */
    @SuppressWarnings("UnusedReturnValue")
    default ClinicVO exactlyFindByCode(String clinicCode) throws DomainNotFoundException {
        ClinicVO clinic = findByCode(clinicCode);
        DomainNotFoundException.assertFound(clinic, clinicCode);
        return clinic;
    }

}
