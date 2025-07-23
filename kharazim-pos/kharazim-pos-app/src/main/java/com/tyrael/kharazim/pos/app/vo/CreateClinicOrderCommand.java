package com.tyrael.kharazim.pos.app.vo;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/7/18
 */
public record CreateClinicOrderCommand(String clinicCode,
                                       String currency,
                                       @Nullable Long customerId,
                                       @NotEmpty(message = "Need products") List<Product> products,
                                       String note,
                                       String saleUserCode,
                                       List<Discount> orderLevelDiscounts) {

    public record Product(@NotEmpty(message = "Need a product") String sku,
                          @Min(value = 1, message = "The minimum quantity is 1")
                          @Max(value = 999999, message = "The maximum quantity is 999999")
                          @NotNull(message = "Need a product quantity") Integer quantity,
                          List<Discount> productDiscounts) {
    }

    public record Discount(@NotNull Long discountId,
                           @NotNull Integer discountVersion) {
    }

}
