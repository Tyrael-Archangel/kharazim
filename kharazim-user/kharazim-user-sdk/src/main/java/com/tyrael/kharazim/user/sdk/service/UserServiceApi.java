package com.tyrael.kharazim.user.sdk.service;

import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.user.sdk.model.UserSimpleVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Tyrael Archangel
 * @since 2025/3/4
 */
public interface UserServiceApi {


    /**
     * map by codes
     *
     * @param codes codes
     * @return Map<code, UserSimpleVO>
     */
    Map<String, UserSimpleVO> mapByCodes(Collection<String> codes);

    /**
     * find by code
     *
     * @param code code
     * @return UserSimpleVO
     */
    default UserSimpleVO findByCode(String code) {
        return mapByCodes(List.of(code)).get(code);
    }

    /**
     * 验证用户存在
     *
     * @param code 会员编码
     */
    default void ensureUserExist(String code) throws DomainNotFoundException {
        UserSimpleVO user = findByCode(code);
        DomainNotFoundException.assertFound(user, code);
    }

}
