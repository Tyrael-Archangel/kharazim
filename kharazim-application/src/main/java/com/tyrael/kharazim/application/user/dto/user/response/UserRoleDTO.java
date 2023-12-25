package com.tyrael.kharazim.application.user.dto.user.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@Builder
public class UserRoleDTO {

    private Long userId;
    private Long roleId;
    private boolean admin;
    private String roleCode;
    private String roleName;

}
