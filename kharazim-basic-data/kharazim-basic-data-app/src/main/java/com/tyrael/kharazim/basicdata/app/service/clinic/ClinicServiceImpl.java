package com.tyrael.kharazim.basicdata.app.service.clinic;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcelFactory;
import cn.idev.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.basicdata.app.constant.BasicDataBusinessIdConstants;
import com.tyrael.kharazim.basicdata.app.domain.clinic.Clinic;
import com.tyrael.kharazim.basicdata.app.dto.clinic.*;
import com.tyrael.kharazim.basicdata.app.mapper.clinic.ClinicMapper;
import com.tyrael.kharazim.basicdata.app.service.file.FileService;
import com.tyrael.kharazim.idgenerator.IdGenerator;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2023/12/30
 */
@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {

    private final ClinicMapper clinicMapper;
    private final FileService fileService;

    @DubboReference
    private IdGenerator idGenerator;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ClinicVO> page(PageClinicRequest pageRequest) {
        Page<Clinic> pageCondition = new Page<>(pageRequest.getPageIndex(), pageRequest.getPageSize());
        PageResponse<Clinic> pageData = clinicMapper.page(pageRequest, pageCondition);

        List<ClinicVO> clinics = pageData.getData()
                .stream()
                .map(this::clinicVO)
                .collect(Collectors.toList());

        return PageResponse.success(clinics, pageData.getTotalCount());
    }

    @Override
    @Transactional(readOnly = true)
    public void export(PageClinicRequest request, HttpServletResponse response) throws IOException {
        WriteSheet writeSheet = FastExcelFactory.writerSheet("诊所数据")
                .head(ClinicExportVO.class)
                .build();
        int pageSize = 200;
        int pageNum = 1;
        try (ExcelWriter excelWriter = FastExcelFactory.write(response.getOutputStream()).build()) {
            List<ClinicExportVO> exports;
            do {
                Page<Clinic> pageCondition = new Page<>(pageNum, pageSize, false);
                PageResponse<Clinic> pageData = clinicMapper.page(request, pageCondition);

                exports = clinicExports(pageData.getData());
                excelWriter.write(exports, writeSheet);

                pageNum++;
            } while (!exports.isEmpty());

            response.addHeader("Content-disposition", "attachment;filename="
                    + URLEncoder.encode("诊所数据.xlsx", StandardCharsets.UTF_8));
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        }
    }

    private List<ClinicExportVO> clinicExports(Collection<Clinic> clinics) throws IOException {
        List<ClinicExportVO> exports = new ArrayList<>();
        for (Clinic clinic : clinics) {
            ClinicExportVO exportVO = ClinicExportVO.builder()
                    .code(clinic.getCode())
                    .image(fileService.readBytes(clinic.getImage()))
                    .name(clinic.getName())
                    .englishName(clinic.getEnglishName())
                    .status(clinic.getStatus())
                    .build();
            exports.add(exportVO);
        }
        return exports;
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
                .image(clinic.getImage())
                .imageUrl(fileService.getUrl(clinic.getImage()))
                .status(clinic.getStatus())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(AddClinicRequest addClinicRequest) {

        String code = idGenerator.next(BasicDataBusinessIdConstants.CLINIC);

        Clinic clinic = new Clinic();
        clinic.setCode(code);
        clinic.setName(addClinicRequest.getName());
        clinic.setEnglishName(addClinicRequest.getEnglishName());
        clinic.setImage(addClinicRequest.getImage());
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

        if (StringUtils.isNotBlank(modifyClinicRequest.getName())) {
            clinic.setName(modifyClinicRequest.getName());
        }
        if (StringUtils.isNotBlank(modifyClinicRequest.getEnglishName())) {
            clinic.setEnglishName(modifyClinicRequest.getEnglishName());
        }
        if (StringUtils.isNotBlank(modifyClinicRequest.getImage())) {
            clinic.setImage(modifyClinicRequest.getImage());
        }
        if (modifyClinicRequest.getStatus() != null) {
            clinic.setStatus(modifyClinicRequest.getStatus());
        }
        clinic.setUpdateUser(currentUser.getCode(), currentUser.getNickName());

        try {
            clinicMapper.updateById(clinic);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("诊所（机构）名称已存在", e);
        }
    }

}
