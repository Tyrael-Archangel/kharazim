package com.tyrael.kharazim.user.app.service;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.user.app.dto.role.request.SaveRoleRequest;
import com.tyrael.kharazim.user.app.dto.role.response.RoleDetailDTO;
import com.tyrael.kharazim.user.app.dto.role.response.RolePageDTO;
import com.tyrael.kharazim.user.app.dto.user.request.PageRoleRequest;

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

    /**
     * 角色（岗位）详情
     *
     * @param id 角色（岗位）ID
     * @return 角色（岗位）详情
     */
    RoleDetailDTO roleDetail(Long id);

    /**
     * 新增角色（岗位）
     *
     * @param addRoleRequest AddRoleRequest
     * @return 新增的角色（岗位）ID
     */
    Long add(SaveRoleRequest addRoleRequest);

    /**
     * 修改角色（岗位）
     *
     * @param id                角色（岗位）ID
     * @param modifyRoleRequest ModifyRoleRequest
     */
    void modify(Long id, SaveRoleRequest modifyRoleRequest);

    /**
     * 删除角色（岗位）
     *
     * @param id 角色（岗位）ID
     */
    void delete(Long id);

    /**
     * 启用角色（岗位）
     *
     * @param id 角色（岗位）ID
     */
    void enable(Long id);

    /**
     * 禁用角色（岗位）
     *
     * @param id 角色（岗位）ID
     */
    void disable(Long id);

}
