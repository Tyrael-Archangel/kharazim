package com.tyrael.kharazim.application.prescription.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyrael.kharazim.application.prescription.domain.Prescription;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
@Mapper
public interface PrescriptionMapper extends BaseMapper<Prescription> {
}
