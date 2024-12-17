package com.tyrael.kharazim.application.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.base.BasePageMapper;
import com.tyrael.kharazim.application.user.domain.Role;
import com.tyrael.kharazim.application.user.dto.user.request.PageRoleRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Mapper
public interface RoleMapper extends BasePageMapper<Role> {

    /**
     * list by roleCodes
     *
     * @param codes roleCodes
     * @return Roles
     */
    default List<Role> listByCodes(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return List.of();
        }
        LambdaQueryWrapper<Role> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Role::getCode, codes);
        queryWrapper.eq(Role::getDeletedTimestamp, 0L);
        return selectList(queryWrapper);
    }

    /**
     * list by IDs
     *
     * @param ids ID
     * @return Roles
     */
    default List<Role> listByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Role> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Role::getDeletedTimestamp, 0L);
        queryWrapper.in(Role::getId, ids);
        return selectList(queryWrapper);
    }

    /**
     * 角色（岗位）分页查询
     *
     * @param pageRoleRequest PageRoleRequest
     * @return 角色（岗位）分页数据
     */
    default PageResponse<Role> page(PageRoleRequest pageRoleRequest) {
        LambdaQueryWrapper<Role> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Role::getDeletedTimestamp, 0L);
        String keywords = pageRoleRequest.getKeywords();
        if (StringUtils.isNotBlank(keywords)) {
            queryWrapper.and(q -> q.like(Role::getName, keywords)
                    .or()
                    .like(Role::getCode, keywords));
        }

        queryWrapper.orderByAsc(Role::getSort);

        return page(pageRoleRequest, queryWrapper);
    }

    /**
     * all roles
     *
     * @return all roles
     */
    default List<Role> listAll() {
        LambdaQueryWrapper<Role> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Role::getDeletedTimestamp, 0L);
        return selectList(queryWrapper);
    }

    /**
     * 逻辑删除
     *
     * @param id 角色（岗位）ID
     */
    default void logicDelete(Long id) {
        LambdaUpdateWrapper<Role> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Role::getId, id)
                .set(Role::getDeletedTimestamp, System.currentTimeMillis());
        this.update(null, updateWrapper);
    }

}
