package com.tyrael.kharazim.application.prescription.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.prescription.domain.Prescription;
import com.tyrael.kharazim.application.prescription.vo.PagePrescriptionRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.util.DateTimeUtil;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
@Mapper
public interface PrescriptionMapper extends BaseMapper<Prescription> {

    /**
     * find by code
     *
     * @param code 处方编码
     * @return Prescription
     */
    default Prescription findByCode(String code) {
        LambdaQueryWrapper<Prescription> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Prescription::getCode, code);
        return selectOne(queryWrapper);
    }

    /**
     * 处方分页查询
     *
     * @param pageRequest   {@link PagePrescriptionRequest}
     * @param pageCondition 分页条件
     * @return 处方分页数据
     */
    default PageResponse<Prescription> page(PagePrescriptionRequest pageRequest, Page<Prescription> pageCondition) {
        LambdaQueryWrapperX<Prescription> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfHasText(Prescription::getCode, pageRequest.getPrescriptionCode());
        queryWrapper.eqIfHasText(Prescription::getCustomerCode, pageRequest.getCustomerCode());
        queryWrapper.inIfPresent(Prescription::getClinicCode, pageRequest.getClinicCodes());
        queryWrapper.geIfPresent(Prescription::getCreateTime,
                DateTimeUtil.startTimeOfDate(pageRequest.getCreateDateMin()));
        queryWrapper.leIfPresent(Prescription::getCreateTime,
                DateTimeUtil.endTimeOfDate(pageRequest.getCreateDateMax()));

        queryWrapper.orderByDesc(Prescription::getCode);

        Page<Prescription> pageData = selectPage(pageCondition, queryWrapper);
        return PageResponse.success(pageData.getRecords(),
                pageData.getTotal(),
                (int) pageCondition.getSize(),
                (int) pageCondition.getCurrent());
    }

}
