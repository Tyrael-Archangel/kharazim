package com.tyrael.kharazim.authentication;

/**
 * @author Tyrael Archangel
 * @since 2025/3/5
 */
public interface Principal {

    /**
     * get id
     *
     * @return id
     */
    Long getId();

    /**
     * get code
     *
     * @return code
     */
    String getCode();

    /**
     * get nickName
     *
     * @return nickName
     */
    String getNickName();

    /**
     * get token
     *
     * @return token
     */
    String getToken();

}
