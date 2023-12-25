package com.tyrael.kharazim.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MultiResponse<T> extends DataResponse<Collection<T>> {

    private int totalCount;

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
        return data == null ? Collections.emptyList() : data;
    }

}
