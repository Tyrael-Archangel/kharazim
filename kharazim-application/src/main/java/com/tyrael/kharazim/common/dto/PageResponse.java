package com.tyrael.kharazim.common.dto;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageResponse<T> extends MultiResponse<T> {

    private int currentPageCount;

    public static <T> PageResponse<T> success(Collection<T> data, int totalCount) {
        PageResponse<T> response = new PageResponse<>();
        response.markSuccess();
        response.data = data;
        response.currentPageCount = data == null ? 0 : data.size();
        response.totalCount = totalCount;
        return response;
    }

    public static <T> PageResponse<T> success(Collection<T> data, long totalCount) {
        return success(data, (int) totalCount);
    }

    public static <T> PageResponse<T> error(String msg) {
        PageResponse<T> response = new PageResponse<>();
        response.markError(msg);
        response.data = null;
        return response;
    }

    @Override
    public Collection<T> getData() {
        return Optional.ofNullable(super.getData())
                .orElseGet(Lists::newArrayList);
    }
}
