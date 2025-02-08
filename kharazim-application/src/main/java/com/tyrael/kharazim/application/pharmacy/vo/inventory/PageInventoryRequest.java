package com.tyrael.kharazim.application.pharmacy.vo.inventory;

import com.tyrael.kharazim.common.dto.SortPageCommand;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Data
public class PageInventoryRequest extends SortPageCommand<PageInventoryRequest.SortBy> {

    @Schema(description = "SKU编码")
    private String skuCode;

    @Schema(description = "商品名称")
    private String skuName;

    @ArraySchema(arraySchema = @Schema(description = "诊所编码"))
    private List<String> clinicCodes;

    @Schema(description = "排序方式, " + SortBy.DESCRIPTION, implementation = SortBy.class)
    private SortBy sortBy = SortBy.QUANTITY;

    @Schema(hidden = true)
    protected SortBy getDefaultSortBy() {
        return SortBy.QUANTITY;
    }

    public enum SortBy {
        /**
         * 在库库存
         */
        QUANTITY,
        /**
         * 预占库存
         */
        OCCUPIED_QUANTITY,
        /**
         * 可用库存
         */
        USABLE_QUANTITY;

        public static final String DESCRIPTION = "QUANTITY-在库库存，OCCUPIED_QUANTITY-预占库存，USABLE_QUANTITY-可用库存";

    }

}
