package com.tyrael.kharazim.diagnosistreatment.app.vo.prescription;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Tyrael Archangel
 * @since 2025/6/17
 */
public record DailySalesModels() {

    @Data
    public static class FilterCommand {

        @Schema(description = "诊所编码")
        private String clinicCode;

        @Schema(description = "开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "请指定开始日期")
        private LocalDate start;

        @Schema(description = "结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "请指定结束日期")
        private LocalDate end;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class View {

        @Schema(description = "日期")
        private LocalDate date;

        @Schema(description = "金额")
        private BigDecimal amount;

    }

}
