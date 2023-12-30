package com.tyrael.kharazim.application.clinic.service.impl;

import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.clinic.service.ClinicService;
import com.tyrael.kharazim.application.clinic.vo.AddClinicRequest;
import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Tyrael Archangel
 * @since 2023/12/30
 */
@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {

    private final CodeGenerator codeGenerator;
    private final ClinicMapper clinicMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(AddClinicRequest addClinicRequest) {

        String code = codeGenerator.next(BusinessCodeConstants.CLINIC);

        Clinic clinic = new Clinic();
        clinic.setCode(code);
        clinic.setName(addClinicRequest.getName());
        clinic.setEnglishName(addClinicRequest.getEnglishName());
        clinic.setStatus(addClinicRequest.getStatusOrDefault());

        try {
            clinicMapper.insert(clinic);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("诊所（机构）名称已存在", e);
        }
        return clinic.getCode();
    }

}
