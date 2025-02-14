package com.tyrael.kharazim.user.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.lib.base.dto.PageResponse;
import com.tyrael.kharazim.lib.base.util.CollectionUtils;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import com.tyrael.kharazim.user.app.domain.Role;
import com.tyrael.kharazim.user.app.dto.user.request.PageRoleRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
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
        String keywords = pageRoleRequest.getKeywords();
        if (StringUtils.isNotBlank(keywords)) {
            queryWrapper.and(q -> q.like(Role::getName, keywords)
                    .or().like(Role::getCode, keywords));
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
        return selectList(Wrappers.lambdaQuery());
    }

}
