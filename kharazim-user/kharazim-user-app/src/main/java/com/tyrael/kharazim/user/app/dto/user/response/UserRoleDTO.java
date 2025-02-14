package com.tyrael.kharazim.user.app.dto.user.response;

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
    private Boolean admin;
    private String roleCode;
    private String roleName;

    public boolean isAdmin() {
        return Boolean.TRUE.equals(admin);
    }

}
