package com.tyrael.kharazim.user.app.dto.auth;

import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2025/1/7
 */
@Data
public class LoginClientInfo {

    private String host;
    private String os;
    private String browser;
    private String browserVersion;

}
