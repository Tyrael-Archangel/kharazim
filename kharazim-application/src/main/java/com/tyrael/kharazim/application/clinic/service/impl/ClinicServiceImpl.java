package com.tyrael.kharazim.application.clinic.service.impl;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.clinic.service.ClinicService;
import com.tyrael.kharazim.application.clinic.vo.*;
import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public PageResponse<ClinicVO> page(PageClinicRequest pageRequest) {
        PageResponse<Clinic> pageData = clinicMapper.page(pageRequest);

        List<ClinicVO> clinics = pageData.getData()
                .stream()
                .map(this::clinicVO)
                .collect(Collectors.toList());

        return PageResponse.success(clinics,
                pageData.getTotalCount(),
                pageData.getPageSize(),
                pageData.getPageNum());
    }

    @Override
    public List<ClinicVO> list(ListClinicRequest request) {
        List<Clinic> clinics = clinicMapper.list(request);
        return clinics.stream()
                .map(this::clinicVO)
                .collect(Collectors.toList());
    }

    private ClinicVO clinicVO(Clinic clinic) {
        return ClinicVO.builder()
                .code(clinic.getCode())
                .name(clinic.getName())
                .englishName(clinic.getEnglishName())
                .status(clinic.getStatus())
                .build();
    }

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(ModifyClinicRequest modifyClinicRequest, AuthUser currentUser) {
        Clinic clinic = clinicMapper.exactlyFindByCode(modifyClinicRequest.getCode());

        clinic.setName(modifyClinicRequest.getName());
        clinic.setEnglishName(modifyClinicRequest.getEnglishName());
        clinic.setStatus(modifyClinicRequest.getStatusOrDefault());
        clinic.setUpdate(currentUser.getCode(), currentUser.getNickName());

        try {
            clinicMapper.updateById(clinic);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("诊所（机构）名称已存在", e);
        }
    }

}
