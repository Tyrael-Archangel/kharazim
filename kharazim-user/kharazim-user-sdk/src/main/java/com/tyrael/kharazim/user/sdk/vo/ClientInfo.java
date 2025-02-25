package com.tyrael.kharazim.user.sdk.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Tyrael Archangel
 * @since 2025/1/7
 */
@Data
public class ClientInfo implements Serializable {

    private String host;
    private String os;
    private String browser;
    private String browserVersion;

}
