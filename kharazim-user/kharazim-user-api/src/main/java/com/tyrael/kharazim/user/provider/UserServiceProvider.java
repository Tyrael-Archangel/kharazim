package com.tyrael.kharazim.user.provider;

import com.tyrael.kharazim.user.app.dto.user.request.ListUserRequest;
import com.tyrael.kharazim.user.app.dto.user.response.UserDTO;
import com.tyrael.kharazim.user.app.service.UserService;
import com.tyrael.kharazim.user.sdk.model.UserSimpleVO;
import com.tyrael.kharazim.user.sdk.service.UserServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/2/11
 */
@Service
@DubboService
@RequiredArgsConstructor
public class UserServiceProvider implements UserServiceApi {

    private final UserService userService;

    @Override
    public Map<String, UserSimpleVO> mapByCodes(Collection<String> codes) {
        List<UserDTO> users = userService.listByCodes(codes);
        return users.stream().collect(Collectors.toMap(
                UserDTO::getCode,
                e -> UserSimpleVO.builder()
                        .id(e.getId())
                        .code(e.getCode())
                        .name(e.getName())
                        .nickName(e.getNickName())
                        .englishName(e.getEnglishName())
                        .avatar(e.getAvatar())
                        .build()
        ));
    }

    @Override
    public List<UserSimpleVO> listAll() {
        ListUserRequest listRequest = new ListUserRequest();
        listRequest.setKeywords(null);
        listRequest.setStatus(null);
        List<UserDTO> users = userService.list(listRequest);
        return users.stream()
                .map(e -> UserSimpleVO.builder()
                        .id(e.getId())
                        .code(e.getCode())
                        .name(e.getName())
                        .nickName(e.getNickName())
                        .englishName(e.getEnglishName())
                        .avatar(e.getAvatar())
                        .build())
                .collect(Collectors.toList());
    }

}
