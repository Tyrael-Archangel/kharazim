package com.tyrael.kharazim.user.app.service.impl;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.base.exception.ForbiddenException;
import com.tyrael.kharazim.idgenerator.IdGenerator;
import com.tyrael.kharazim.user.app.domain.Role;
import com.tyrael.kharazim.user.app.dto.role.request.SaveRoleRequest;
import com.tyrael.kharazim.user.app.dto.role.response.RoleDetailDTO;
import com.tyrael.kharazim.user.app.dto.role.response.RolePageDTO;
import com.tyrael.kharazim.user.app.dto.user.request.PageRoleRequest;
import com.tyrael.kharazim.user.app.enums.EnableStatusEnum;
import com.tyrael.kharazim.user.app.constant.UserBusinessIdConstants;
import com.tyrael.kharazim.user.app.mapper.RoleMapper;
import com.tyrael.kharazim.user.app.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static com.tyrael.kharazim.user.app.config.CacheKeyConstants.CURRENT_USER_INFO;

/**
 * @author Tyrael Archangel
 * @since 2024/1/4
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final TransactionTemplate transactionTemplate;

    @DubboReference
    private IdGenerator idGenerator;

    @Override
    public PageResponse<RolePageDTO> rolePage(PageRoleRequest pageRoleRequest) {
        PageResponse<Role> rolePage = roleMapper.page(pageRoleRequest);
        List<RolePageDTO> roles = rolePage.getData()
                .stream()
                .map(e -> RolePageDTO.builder()
                        .id(e.getId())
                        .code(e.getCode())
                        .name(e.getName())
                        .status(e.getStatus())
                        .sort(e.getSort())
                        .createTime(e.getCreateTime())
                        .updateTime(e.getUpdateTime())
                        .build())
                .toList();
        return PageResponse.success(roles, rolePage.getTotalCount());
    }

    @Override
    public RoleDetailDTO roleDetail(Long id) {
        Role role = getById(id);
        return RoleDetailDTO.builder()
                .id(role.getId())
                .code(role.getCode())
                .name(role.getName())
                .sort(role.getSort())
                .status(role.getStatus())
                .build();
    }

    @Override
    public Long add(SaveRoleRequest addRoleRequest) {

        String code = idGenerator.next(UserBusinessIdConstants.ROLE);

        Role role = new Role();
        role.setCode(code);
        role.setSuperAdmin(false);
        role.setName(addRoleRequest.getName());
        role.setSort(addRoleRequest.getSort());
        role.setStatus(addRoleRequest.getStatus());

        transactionTemplate.executeWithoutResult(status -> {
            try {
                roleMapper.insert(role);
            } catch (DuplicateKeyException e) {
                throw new BusinessException("角色（岗位）名已存在", e);
            }
        });

        return role.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CURRENT_USER_INFO}, allEntries = true)
    public void modify(Long id, SaveRoleRequest modifyRoleRequest) {
        Role role = getById(id);
        if (role.isAdmin()) {
            throw new ForbiddenException("超级管理员无法修改");
        }

        role.setName(modifyRoleRequest.getName());
        role.setSort(modifyRoleRequest.getSort());
        role.setStatus(modifyRoleRequest.getStatus());
        role.setUpdateTime(LocalDateTime.now());

        try {
            roleMapper.updateById(role);
        } catch (DuplicateKeyException e) {
            // 把角色（岗位）名修改为已存在的角色（岗位）名
            throw new BusinessException("角色（岗位）名已存在", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CURRENT_USER_INFO}, allEntries = true)
    public void delete(Long id) {
        roleMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CURRENT_USER_INFO}, allEntries = true)
    public void enable(Long id) {
        Role role = getById(id);
        if (role.isAdmin()) {
            throw new ForbiddenException("超级管理员无法修改");
        }
        role.setStatus(EnableStatusEnum.ENABLED);
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CURRENT_USER_INFO}, allEntries = true)
    public void disable(Long id) {
        Role role = getById(id);
        if (role.isAdmin()) {
            throw new ForbiddenException("超级管理员无法修改");
        }
        role.setStatus(EnableStatusEnum.DISABLED);
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.updateById(role);
    }

    private Role getById(Long id) {
        Role role = roleMapper.selectById(id);
        DomainNotFoundException.assertFound(role, id);
        return role;
    }
}
