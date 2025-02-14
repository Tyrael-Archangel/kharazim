package com.tyrael.kharazim.lib.base.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

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

    public static <T> PageResponse<T> error(String msg) {
        PageResponse<T> response = new PageResponse<>();
        response.markError(msg);
        response.data = null;
        return response;
    }

}
