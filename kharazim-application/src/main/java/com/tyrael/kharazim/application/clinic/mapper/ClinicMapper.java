package com.tyrael.kharazim.application.clinic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.vo.PageClinicRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2023/12/30
 */
@Mapper
public interface ClinicMapper extends BaseMapper<Clinic> {
    /**
     * 分页查询
     *
     * @param pageRequest {@link PageClinicRequest}
     * @return 分页数据
     */
    default PageResponse<Clinic> page(PageClinicRequest pageRequest) {

        LambdaQueryWrapperX<Clinic> queryWrapper = new LambdaQueryWrapperX<>();
        String name = pageRequest.getName();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.and(q -> q.like(Clinic::getName, name)
                    .or()
                    .like(Clinic::getEnglishName, name));
        }
        queryWrapper.eqIfPresent(Clinic::getStatus, pageRequest.getStatus());
        queryWrapper.orderByAsc(Clinic::getCode);

        Page<Clinic> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<Clinic> pageResponse = selectPage(page, queryWrapper);
        return PageResponse.success(pageResponse.getRecords(),
                pageResponse.getTotal(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }

}
