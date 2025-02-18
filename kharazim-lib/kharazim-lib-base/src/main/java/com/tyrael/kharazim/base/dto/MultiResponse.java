package com.tyrael.kharazim.base.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MultiResponse<T> extends DataResponse<Collection<T>> {

    protected int totalCount;

    public static <T> MultiResponse<T> success(Collection<T> data) {
        MultiResponse<T> response = new MultiResponse<>();
        response.markSuccess();
        response.data = data;
        response.totalCount = data == null ? 0 : data.size();
        return response;
    }

    public static <T> MultiResponse<T> error(String msg) {
        MultiResponse<T> response = new MultiResponse<>();
        response.markError(msg);
        return response;
    }

    @Override
    public Collection<T> getData() {
        return data == null ? new ArrayList<>() : data;
    }

}
