package com.tyrael.kharazim.application.user.service;

import com.tyrael.kharazim.application.user.dto.role.request.SaveRoleRequest;
import com.tyrael.kharazim.application.user.dto.role.response.RoleDetailDTO;
import com.tyrael.kharazim.application.user.dto.role.response.RolePageDTO;
import com.tyrael.kharazim.application.user.dto.user.request.PageRoleRequest;
import com.tyrael.kharazim.common.dto.PageResponse;

import java.util.List;

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
     * @param ids 角色（岗位）ID
     */
    void delete(List<Long> ids);

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

    /**
     * 获取角色的菜单ID
     *
     * @param id 角色ID
     * @return 角色的菜单ID
     */
    List<Long> getRoleMenuIds(Long id);

    /**
     * 更新角色的菜单
     *
     * @param id      角色ID
     * @param menuIds 菜单ID
     */
    void updateRoleMenus(Long id, List<Long> menuIds);

}
