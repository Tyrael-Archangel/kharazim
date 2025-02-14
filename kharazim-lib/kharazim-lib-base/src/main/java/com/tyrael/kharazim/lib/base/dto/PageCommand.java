package com.tyrael.kharazim.lib.base.dto;

import com.tyrael.kharazim.lib.base.exception.BusinessException;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
public class PageCommand {

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 2000;
    public static final int DEFAULT_PAGE_NUM = 1;

    protected Integer pageSize;

    protected Integer pageIndex;

    public Integer getPageSize() {
        if (pageSize == null) {
            return DEFAULT_PAGE_SIZE;
        }
        if (pageSize > MAX_PAGE_SIZE) {
            throw new BusinessException("pageSize too big");
        }
        return pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex == null ? DEFAULT_PAGE_NUM : pageIndex;
    }

}
