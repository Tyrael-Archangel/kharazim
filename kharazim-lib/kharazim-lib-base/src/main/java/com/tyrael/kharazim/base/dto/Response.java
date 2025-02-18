package com.tyrael.kharazim.base.dto;

import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
@Data
public class Response {

    public static final int SUCCESS_CODE = 200;
    public static final int BUSINESS_ERROR_CODE = 400;

    protected int code;
    protected boolean success;
    protected String msg;

    public static Response success() {
        Response response = new Response();
        response.markSuccess();
        return response;
    }

    public static Response error(int code, String msg) {
        Response response = new Response();
        response.markError(code, msg);
        return response;
    }

    protected void markSuccess() {
        this.code = SUCCESS_CODE;
        this.success = true;
        this.msg = "success";
    }

    protected void markError(int code, String msg) {
        this.code = code;
        this.success = false;
        this.msg = msg;
    }

    protected void markError(String msg) {
        this.markError(BUSINESS_ERROR_CODE, msg);
    }

}