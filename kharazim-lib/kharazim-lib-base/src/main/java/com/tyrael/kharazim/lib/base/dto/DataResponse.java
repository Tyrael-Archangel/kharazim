package com.tyrael.kharazim.lib.base.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class DataResponse<T> extends Response {

    protected T data;

    public static <T> DataResponse<T> success(T data) {
        DataResponse<T> response = new DataResponse<>();
        response.markSuccess();
        response.data = data;
        return response;
    }

    public static <T> DataResponse<T> fail(String msg) {
        return fail(null, msg);
    }

    public static <T> DataResponse<T> fail(T data, String msg) {
        DataResponse<T> response = new DataResponse<>();
        response.markError(msg);
        response.data = data;
        return response;
    }

    public static <T> DataResponse<T> fail(T data, int code, String msg) {
        DataResponse<T> response = new DataResponse<>();
        response.markError(code, msg);
        response.data = data;
        return response;
    }

}
