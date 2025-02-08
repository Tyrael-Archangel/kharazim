package com.tyrael.kharazim.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2025/2/8
 */
@Data
public abstract class SortPageCommand<S> extends PageCommand {

    @Schema(description = "排序方式")
    private S sortBy;

    @Schema(description = "排序方向, " + SortDirection.DESCRIPTION, implementation = SortDirection.class)
    private SortDirection sortDirection = SortDirection.DESC;

    @Schema(hidden = true)
    public S getSortByOrDefault() {
        return sortBy != null ? sortBy : getDefaultSortBy();
    }

    @Schema(hidden = true)
    protected abstract S getDefaultSortBy();

    public boolean isAsc() {
        return sortDirection == SortDirection.ASC;
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
