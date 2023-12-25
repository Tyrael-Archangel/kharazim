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

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 2000;
    private static final int DEFAULT_PAGE_NUM = 1;

    @Schema(description = "分页-每页数据条数，默认10", example = "10")
    protected Integer pageSize;

    @Schema(description = "分页-页码，默认1", example = "1")
    protected Integer pageNum;

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
    public Integer getPageNum() {
        return pageNum == null ? DEFAULT_PAGE_NUM : pageNum;
    }

}
