package com.tyrael.kharazim.application.user.service;

import com.tyrael.kharazim.application.user.dto.role.response.RolePageDTO;
import com.tyrael.kharazim.application.user.dto.user.request.PageRoleRequest;
import com.tyrael.kharazim.common.dto.PageResponse;

/**
 * @author Tyrael Archangel
 * @since 2024/1/4
 */
public interface RoleService {

    /**
     * 角色（岗位）分页
     *
     * @param pageRoleRequest PageRoleRequest
     * @return 角色（岗位）分页数据
     */
    PageResponse<RolePageDTO> rolePage(PageRoleRequest pageRoleRequest);

}
