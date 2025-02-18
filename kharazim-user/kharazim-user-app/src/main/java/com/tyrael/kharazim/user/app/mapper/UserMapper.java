package com.tyrael.kharazim.user.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import com.tyrael.kharazim.mybatis.LambdaQueryWrapperX;
import com.tyrael.kharazim.user.app.domain.User;
import com.tyrael.kharazim.user.app.dto.user.request.ListUserRequest;
import com.tyrael.kharazim.user.app.dto.user.request.PageUserRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Mapper
public interface UserMapper extends BasePageMapper<User> {

    /**
     * find by code
     *
     * @param code code
     * @return User
     */
    default User findByCode(String code) {

        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getCode, code);

        return selectOne(queryWrapper);
    }

    /**
     * 验证用户存在
     *
     * @param code 会员编码
     */
    default void ensureUserExist(String code) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getCode, code);
        if (!this.exists(queryWrapper)) {
            throw new DomainNotFoundException("userCode: " + code);
        }
    }

    /**
     * list by code
     *
     * @param codes codes
     * @return Users
     */
    default List<User> listByCodes(Collection<String> codes) {

        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(User::getCode, codes);

        return selectList(queryWrapper);
    }

    /**
     * map by codes
     *
     * @param codes codes
     * @return Map<code, User>
     */
    default Map<String, User> mapByCodes(Collection<String> codes) {
        List<User> users = listByCodes(codes);
        return users.stream()
                .collect(Collectors.toMap(User::getCode, e -> e));
    }

    /**
     * find by name
     *
     * @param name name
     * @return User
     */
    default User findByName(String name) {

        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getName, name);

        return selectOne(queryWrapper);
    }

    /**
     * page user
     *
     * @param pageCommand PageUserRequest
     * @return Page of users
     */
    default PageResponse<User> page(PageUserRequest pageCommand) {

        LambdaQueryWrapperX<User> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfPresent(User::getStatus, pageCommand.getStatus());
        queryWrapper.eqIfPresent(User::getGender, pageCommand.getGender());

        String keywords = StringUtils.trim(pageCommand.getKeywords());
        if (StringUtils.isNotBlank(keywords)) {
            queryWrapper.and(q -> q.like(User::getName, keywords)
                    .or().like(User::getNickName, keywords)
                    .or().like(User::getPhone, keywords));
        }
        queryWrapper.orderByAsc(User::getCode);

        return page(pageCommand, queryWrapper);
    }

    /**
     * list
     *
     * @param listRequest {@link ListUserRequest}
     * @return Users
     */
    default List<User> list(ListUserRequest listRequest) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        String keywords = StringUtils.trim(listRequest.getKeywords());
        if (StringUtils.isNotBlank(keywords)) {
            queryWrapper.and(q -> q.like(User::getName, keywords)
                    .or().like(User::getNickName, keywords)
                    .or().like(User::getPhone, keywords));
        }
        if (listRequest.getStatus() != null) {
            queryWrapper.eq(User::getStatus, listRequest.getStatus());
        }
        return selectList(queryWrapper);
    }

    /**
     * 保存修改用户
     *
     * @param user 用户
     */
    default void modify(User user) {
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(User::getId, user.getId())
                .set(User::getGender, user.getGender())
                .set(User::getPhone, user.getPhone())
                .set(User::getAvatar, user.getAvatar())
                .set(User::getWechatCode, user.getWechatCode())
                .set(User::getWechatUnionId, user.getWechatUnionId())
                .set(User::getWechatName, user.getWechatName())
                .set(User::getCertificateType, user.getCertificateType())
                .set(User::getCertificateCode, user.getCertificateCode())
                .set(User::getRemark, user.getRemark())
                .set(User::getBirthday, user.getBirthday())
                .set(User::getUpdater, user.getUpdater())
                .set(User::getUpdaterCode, user.getUpdaterCode())
                .set(User::getUpdateTime, user.getUpdateTime());
        this.update(null, updateWrapper);
    }

    /**
     * change password
     *
     * @param user User
     */
    default void changePassword(User user) {
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(User::getPassword, user.getPassword());
        updateWrapper.set(User::getNeedChangePassword, user.getNeedChangePassword());
        updateWrapper.eq(User::getId, user.getId());
        updateWrapper.set(User::getUpdateTime, LocalDateTime.now());
        this.update(null, updateWrapper);
    }

    /**
     * 修改用户状态
     *
     * @param user user
     */
    default void updateStatus(User user) {
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(User::getStatus, user.getStatus());
        updateWrapper.eq(User::getId, user.getId());
        updateWrapper.set(User::getUpdateTime, LocalDateTime.now());
        this.update(null, updateWrapper);
    }

}
