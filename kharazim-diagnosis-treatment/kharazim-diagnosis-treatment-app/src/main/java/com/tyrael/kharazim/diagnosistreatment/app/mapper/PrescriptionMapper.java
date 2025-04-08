package com.tyrael.kharazim.diagnosistreatment.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.diagnosistreatment.app.domain.prescription.Prescription;
import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.PagePrescriptionRequest;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import com.tyrael.kharazim.mybatis.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;

import static com.tyrael.kharazim.base.util.DateTimeUtil.endTimeOfDate;
import static com.tyrael.kharazim.base.util.DateTimeUtil.startTimeOfDate;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
@Mapper
public interface PrescriptionMapper extends BasePageMapper<Prescription> {

    /**
     * find all with cursor
     *
     * @return Prescription cursor
     */
    @Select("select * from prescription")
    Cursor<Prescription> findAll();

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
     * find by code FOR UPDATE
     *
     * @param code 处方编码
     * @return Prescription
     */
    default Prescription findByCodeForUpdate(String code) {
        LambdaQueryWrapper<Prescription> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Prescription::getCode, code);
        queryWrapper.last(" FOR UPDATE ");
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
        queryWrapper.geIfPresent(Prescription::getCreateTime, startTimeOfDate(pageRequest.getCreateDateMin()));
        queryWrapper.leIfPresent(Prescription::getCreateTime, endTimeOfDate(pageRequest.getCreateDateMax()));

        queryWrapper.orderByDesc(Prescription::getCode);

        return page(pageCondition, queryWrapper);
    }

}
