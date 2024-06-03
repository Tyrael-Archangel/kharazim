package com.tyrael.kharazim.application.clinic.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.vo.ListClinicRequest;
import com.tyrael.kharazim.application.clinic.vo.PageClinicRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import com.tyrael.kharazim.common.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2023/12/30
 */
@Mapper
public interface ClinicMapper extends BaseMapper<Clinic> {

    /**
     * find by code
     *
     * @param code clinicCode
     * @return {@link Clinic}
     */
    default Clinic findByCode(String code) {
        LambdaQueryWrapper<Clinic> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Clinic::getCode, code);
        return selectOne(queryWrapper);
    }

    /**
     * find by code exactly, if not exists throw DomainNotFoundException
     *
     * @param code clinicCode
     * @return {@link Clinic}
     * @throws DomainNotFoundException if code not exists
     */
    default Clinic exactlyFindByCode(String code) throws DomainNotFoundException {
        Clinic clinic = findByCode(code);
        DomainNotFoundException.assertFound(clinic, code);
        return clinic;
    }

    /**
     * 分页查询
     *
     * @param pageRequest   {@link PageClinicRequest}
     * @param pageCondition 分页条件
     * @return 分页数据
     */
    default PageResponse<Clinic> page(PageClinicRequest pageRequest, Page<Clinic> pageCondition) {

        LambdaQueryWrapperX<Clinic> queryWrapper = new LambdaQueryWrapperX<>();
        String name = StringUtils.trim(pageRequest.getName());
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.and(q -> q.like(Clinic::getName, name)
                    .or()
                    .like(Clinic::getEnglishName, name));
        }
        queryWrapper.eqIfPresent(Clinic::getStatus, pageRequest.getStatus());
        queryWrapper.orderByAsc(Clinic::getCode);

        Page<Clinic> pageResponse = selectPage(pageCondition, queryWrapper);
        return PageResponse.success(pageResponse.getRecords(),
                pageResponse.getTotal(),
                (int) pageCondition.getSize(),
                (int) pageCondition.getCurrent());
    }

    /**
     * 列表
     *
     * @param request {@link ListClinicRequest}
     * @return Clinics
     */
    default List<Clinic> list(ListClinicRequest request) {
        LambdaQueryWrapperX<Clinic> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.likeIfPresent(Clinic::getName, StringUtils.trim(request.getName()));
        queryWrapper.eqIfPresent(Clinic::getStatus, request.getStatus());
        return selectList(queryWrapper);
    }

    /**
     * list by code
     *
     * @param codes codes
     * @return Clinics
     */
    default List<Clinic> listByCodes(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<Clinic> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Clinic::getCode, codes);

        return selectList(queryWrapper);
    }

    /**
     * map by codes
     *
     * @param codes codes
     * @return Map<code, Clinic>
     */
    default Map<String, Clinic> mapByCodes(Collection<String> codes) {
        List<Clinic> clinics = listByCodes(codes);
        return clinics.stream()
                .collect(Collectors.toMap(Clinic::getCode, e -> e));
    }

}
