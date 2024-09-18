package com.tyrael.kharazim.common.dto;

import com.tyrael.kharazim.common.exception.BusinessException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.lang.NonNull;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
public class PageCommand {

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 2000;
    public static final int DEFAULT_PAGE_NUM = 1;

    @Schema(description = "分页-每页数据条数，默认10", example = "10")
    protected Integer pageSize;

    @Schema(description = "分页-页码，默认1", example = "1")
    protected Integer pageIndex;

    @NonNull
    public Integer getPageSize() {
        if (pageSize == null) {
            return DEFAULT_PAGE_SIZE;
        }
        if (pageSize > MAX_PAGE_SIZE) {
            throw new BusinessException("pageSize too big");
        }
        return pageSize;
    }

    @NonNull
    public Integer getPageIndex() {
        return pageIndex == null ? DEFAULT_PAGE_NUM : pageIndex;
    }

    public enum SortDirection {
        /**
         * 升序
         */
        ASC,
        /**
         * 降序
         */
        DESC;

        public static final String DESCRIPTION = "ASC-升序，DESC-降序";
    }

}
