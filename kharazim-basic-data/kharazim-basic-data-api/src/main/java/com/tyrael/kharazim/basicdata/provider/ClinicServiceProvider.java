package com.tyrael.kharazim.basicdata.provider;

import com.tyrael.kharazim.basicdata.app.dto.clinic.ClinicDTO;
import com.tyrael.kharazim.basicdata.app.service.clinic.ClinicService;
import com.tyrael.kharazim.basicdata.sdk.model.ClinicVO;
import com.tyrael.kharazim.basicdata.sdk.service.ClinicServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/3/20
 */
@Component
@DubboService
@RequiredArgsConstructor
public class ClinicServiceProvider implements ClinicServiceApi {

    private final ClinicService clinicService;

    @Override
    public List<ClinicVO> listAll() {
        List<ClinicDTO> clinics = clinicService.listAll();
        return clinics.stream()
                .map(this::clinicVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClinicVO> listByCodes(Collection<String> clinicCodes) {
        List<ClinicDTO> clinics = clinicService.listByCodes(clinicCodes);
        return clinics.stream()
                .map(this::clinicVO)
                .collect(Collectors.toList());
    }

    @Override
    public ClinicVO findByCode(String clinicCode) {
        ClinicDTO clinicDTO = clinicService.findByCode(clinicCode);
        return clinicDTO == null ? null : this.clinicVO(clinicDTO);
    }

    private ClinicVO clinicVO(ClinicDTO clinicDTO) {
        return ClinicVO.builder()
                .code(clinicDTO.getCode())
                .name(clinicDTO.getName())
                .englishName(clinicDTO.getEnglishName())
                .image(clinicDTO.getImage())
                .build();
    }

}
