package com.tyrael.kharazim.base.dto;

import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2025/2/8
 */
@Data
public abstract class SortPageCommand<S> extends PageCommand {

    private S sortBy;

    private SortDirection sortDirection = SortDirection.DESC;

    public S getSortByOrDefault() {
        return sortBy != null ? sortBy : getDefaultSortBy();
    }

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
