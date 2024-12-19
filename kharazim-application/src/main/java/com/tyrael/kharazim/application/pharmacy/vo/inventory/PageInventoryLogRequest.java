package com.tyrael.kharazim.application.pharmacy.vo.inventory;

import com.tyrael.kharazim.application.pharmacy.enums.InventoryChangeTypeEnum;
import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/8/29
 */
@Data
public class PageInventoryLogRequest extends PageCommand {

    @Schema(description = "SKU编码")
    private String skuCode;

    @Schema(description = "诊所编码")
    private String clinicCode;

    @Schema(description = "关联业务编码")
    private String businessCode;

    @ArraySchema(arraySchema = @Schema(
            description = "库存变化类型，字典编码: inventory_change_type",
            implementation = InventoryChangeTypeEnum.class))
    private Set<InventoryChangeTypeEnum> changeTypes;

    @Schema(description = "开始时间", format = "yyyy-MM-dd HH:mm:ss", example = "2024-08-01 08:10:30")
    private LocalDateTime startTime;

    @Schema(description = "截止时间", format = "yyyy-MM-dd HH:mm:ss", example = "2024-09-01 23:45:00")
    private LocalDateTime endTime;

}
