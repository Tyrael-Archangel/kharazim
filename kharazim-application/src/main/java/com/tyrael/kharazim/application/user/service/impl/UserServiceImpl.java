package com.tyrael.kharazim.application.user.service.impl;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.config.CacheKeyConstants;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.application.user.converter.UserConverter;
import com.tyrael.kharazim.application.user.domain.Role;
import com.tyrael.kharazim.application.user.domain.User;
import com.tyrael.kharazim.application.user.dto.user.request.*;
import com.tyrael.kharazim.application.user.dto.user.response.CurrentUserDTO;
import com.tyrael.kharazim.application.user.dto.user.response.UserDTO;
import com.tyrael.kharazim.application.user.dto.user.response.UserRoleDTO;
import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import com.tyrael.kharazim.application.user.mapper.RoleMapper;
import com.tyrael.kharazim.application.user.mapper.UserMapper;
import com.tyrael.kharazim.application.user.mapper.UserRoleMapper;
import com.tyrael.kharazim.application.user.service.AuthService;
import com.tyrael.kharazim.application.user.service.UserRoleQueryService;
import com.tyrael.kharazim.application.user.service.UserService;
import com.tyrael.kharazim.application.user.service.component.PasswordEncoder;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import com.tyrael.kharazim.common.exception.ForbiddenException;
import com.tyrael.kharazim.common.util.CollectionUtils;
import com.tyrael.kharazim.common.util.RandomStringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2023/12/27
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final TransactionTemplate transactionTemplate;
    private final CodeGenerator codeGenerator;
    private final UserConverter userConverter;
    private final AuthService authService;
    private final UserRoleQueryService userRoleQueryService;

    @Override
    public UserDTO getById(Long id) {
        User user = userMapper.selectById(id);
        DomainNotFoundException.assertFound(user, id);

        List<UserRoleDTO> userRoles = userRoleQueryService.queryUserRoles(id);
        return userConverter.userDTO(user, userRoles);
    }

    @Override
    @Cacheable(cacheNames = CacheKeyConstants.CURRENT_USER_INFO, key = "#currentUser.id")
    public CurrentUserDTO getCurrentUserInfo(AuthUser currentUser) {
        Long currentUserId = currentUser.getId();
        User user = userMapper.selectById(currentUserId);
        List<UserRoleDTO> userRoles = userRoleQueryService.queryUserRoles(currentUserId);
        LocalDateTime lastLoginTime = authService.getUserLastLoginTime(currentUserId);
        return userConverter.currentUserDTO(user, userRoles, lastLoginTime);
    }

    @Override
    public PageResponse<UserDTO> page(PageUserRequest pageCommand) {
        PageResponse<User> userPage = userMapper.page(pageCommand);
        return PageResponse.success(convertUsers(userPage.getData()), userPage.getTotalCount());
    }

    @Override
    public List<UserDTO> list(ListUserRequest listRequest) {
        List<User> users = userMapper.list(listRequest);
        return convertUsers(users);
    }

    private List<UserDTO> convertUsers(Collection<User> users) {
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        List<Long> userIds = users.stream()
                .map(User::getId)
                .toList();

        List<UserRoleDTO> userRoles = userRoleQueryService.queryUserRoles(userIds);
        Map<Long, List<UserRoleDTO>> userRolesMap = userRoles.stream()
                .collect(Collectors.groupingBy(UserRoleDTO::getUserId));

        return users.stream()
                .map(user -> userConverter.userDTO(user, userRolesMap.get(user.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public String add(AddUserRequest addUserRequest) {

        List<Role> targetRoles = roleMapper.listByCodes(addUserRequest.getRoleCodes());
        boolean isAdmin = targetRoles.stream().anyMatch(Role::isAdmin);
        if (isAdmin) {
            throw new ForbiddenException("不能将用户添加为超级管理员");
        }

        String password = RandomStringUtil.make();
        User user = createUser(addUserRequest, password);

        transactionTemplate.executeWithoutResult(status -> {
            try {
                userMapper.insert(user);
            } catch (DuplicateKeyException e) {
                throw new BusinessException("用户名已存在", e);
            }

            userRoleMapper.save(user.getId(), targetRoles);
        });

        return password;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(ModifyUserRequest modifyUserRequest, AuthUser currentUser) {
        Long userId = modifyUserRequest.getId();
        User user = userMapper.selectById(userId);
        DomainNotFoundException.assertFound(user, userId);

        List<Role> targetRoles = roleMapper.listByCodes(modifyUserRequest.getRoleCodes());
        checkAdminCantAddAndRemove(userId, targetRoles);

        user.setNickName(modifyUserRequest.getNickName());
        user.setEnglishName(modifyUserRequest.getEnglishName());
        user.setAvatar(modifyUserRequest.getAvatar());
        user.setGender(modifyUserRequest.getGender());
        user.setPhone(modifyUserRequest.getPhone());
        user.setCertificateType(modifyUserRequest.getCertificateType());
        user.setCertificateCode(modifyUserRequest.getCertificateCode());
        user.setRemark(modifyUserRequest.getRemark());
        user.setBirthday(modifyUserRequest.getBirthday());
        user.setUpdateUser(currentUser.getCode(), currentUser.getNickName());

        try {
            userMapper.modify(user);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("用户名已存在", e);
        }

        userRoleMapper.save(user.getId(), targetRoles);
    }

    private void checkAdminCantAddAndRemove(Long userId, List<Role> targetRoles) {
        // 如果用户是超管，就不能移除超管，如果不是超管，就不能添加为超管
        List<UserRoleDTO> userRoles = userRoleQueryService.queryUserRoles(userId);
        boolean userIsAdmin = userRoles.stream().anyMatch(UserRoleDTO::isAdmin);

        boolean targetIsAdmin = targetRoles.stream().anyMatch(Role::isAdmin);
        if (userIsAdmin && !targetIsAdmin) {
            throw new BusinessException("不能移除超级管理员");
        }
        if (!userIsAdmin && targetIsAdmin) {
            throw new ForbiddenException("不能将用户添加为超级管理员");
        }
    }

    private User createUser(AddUserRequest addUserRequest, String password) {
        String userCode = codeGenerator.next(BusinessCodeConstants.USER);

        User user = new User();
        user.setCode(userCode);
        user.setName(addUserRequest.getName());
        user.setNickName(addUserRequest.getNickName());
        user.setEnglishName(addUserRequest.getEnglishName());
        user.setAvatar(addUserRequest.getAvatar());
        user.setGender(addUserRequest.getGender());
        user.setBirthday(addUserRequest.getBirthday());
        user.setPhone(addUserRequest.getPhone());
        user.setPassword(passwordEncoder.encode(password));
        user.setNeedChangePassword(true);
        user.setStatus(EnableStatusEnum.ENABLED);
        user.setWechatCode(null);
        user.setWechatUnionId(null);
        user.setWechatName(null);
        user.setCertificateType(addUserRequest.getCertificateType());
        user.setCertificateCode(addUserRequest.getCertificateCode());
        user.setRemark(addUserRequest.getRemark());

        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = CacheKeyConstants.CURRENT_USER_INFO, key = "#currentUser.id")
    public void changePassword(AuthUser currentUser, ChangePasswordRequest changePasswordRequest) {
        User user = userMapper.selectById(currentUser.getId());

        boolean matches = passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword());
        if (!matches) {
            throw new BusinessException("原密码错误");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        user.setNeedChangePassword(false);
        userMapper.changePassword(user);

        authService.logoutByUser(user.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = CacheKeyConstants.CURRENT_USER_INFO, key = "#userId")
    public String resetPassword(AuthUser currentUser, Long userId) {
        if (!currentUser.isSuperAdmin()) {
            throw new ForbiddenException("Not allowed.");
        }

        String newPassword = RandomStringUtil.make();

        User user = new User();
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setNeedChangePassword(true);

        userMapper.changePassword(user);

        return newPassword;
    }

    @Override
    public void updateStatus(Long id, EnableStatusEnum status) {
        User user = userMapper.selectById(id);
        DomainNotFoundException.assertFound(user, id);

        List<UserRoleDTO> userRoles = userRoleQueryService.queryUserRoles(id);
        boolean userIsAdmin = userRoles.stream()
                .anyMatch(UserRoleDTO::isAdmin);
        if (userIsAdmin) {
            throw new ForbiddenException("超级管理员不能被禁用");
        }

        user.setStatus(status);
        userMapper.updateStatus(user);

        if (EnableStatusEnum.DISABLED.equals(status)) {
            authService.logoutByUser(user.getId());
        }
    }

}
