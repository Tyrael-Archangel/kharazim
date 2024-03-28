package com.tyrael.kharazim.application.prescription.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
     * 处方分页查询
     *
     * @param pageRequest {@link PagePrescriptionRequest}
     * @return 处方分页数据
     */
    default PageResponse<Prescription> page(PagePrescriptionRequest pageRequest) {
        LambdaQueryWrapperX<Prescription> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfHasText(Prescription::getCustomerCode, pageRequest.getCustomerCode());
        queryWrapper.eqIfHasText(Prescription::getClinicCode, pageRequest.getClinicCode());
        queryWrapper.geIfPresent(Prescription::getCreateTime,
                DateTimeUtil.startTimeOfDate(pageRequest.getCreateDateMin()));
        queryWrapper.leIfPresent(Prescription::getCreateTime,
                DateTimeUtil.endTimeOfDate(pageRequest.getCreateDateMax()));

        queryWrapper.orderByDesc(Prescription::getCode);

        Page<Prescription> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<Prescription> pageData = selectPage(page, queryWrapper);
        return PageResponse.success(pageData.getRecords(),
                pageData.getTotal(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }

}
