package com.tyrael.kharazim.pos.app.service;

import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
import com.tyrael.kharazim.pos.app.constant.PosBusinessIdConstants;
import com.tyrael.kharazim.pos.app.domain.ClinicOrder;
import com.tyrael.kharazim.pos.app.domain.ClinicOrderLineItem;
import com.tyrael.kharazim.pos.app.enums.ClinicOrderStatusEnum;
import com.tyrael.kharazim.pos.app.mapper.ClinicOrderMapper;
import com.tyrael.kharazim.pos.app.vo.CreateClinicOrderCommand;
import com.tyrael.kharazim.product.sdk.service.ProductServiceApi;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/7/18
 */
@Service
@RequiredArgsConstructor
public class ClinicOrderLifecycleServiceImpl implements ClinicOrderLifecycleService {

    private final ClinicOrderMapper clinicOrderMapper;
    private final IdGenerator idGenerator;

    @DubboReference
    private ProductServiceApi productServiceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(CreateClinicOrderCommand command, AuthUser currentUser) {
        ClinicOrder clinicOrder = new ClinicOrderDomainCreator(command, currentUser).create();
        return clinicOrder.getOrderNumber();
    }

    private class ClinicOrderDomainCreator {

        private final CreateClinicOrderCommand command;
        private final AuthUser currentUser;

        private ClinicOrderDomainCreator(CreateClinicOrderCommand command, AuthUser currentUser) {
            this.command = command;
            this.currentUser = currentUser;
        }

        public ClinicOrder create() {
            ClinicOrder clinicOrder = buildClinicOrder();
            List<ClinicOrderLineItem> clinicOrderLineItems = buildClinicOrderLineItems();
            return clinicOrder;
        }

        private ClinicOrder buildClinicOrder() {

            BigDecimal totalLineItemAmount = totalLineItemAmount();
            BigDecimal totalProductDiscountAmount = totalProductDiscountAmount();
            BigDecimal orderLevelDiscountAmount = orderLevelDiscountAmount();
            BigDecimal subtotalAmount = totalLineItemAmount.subtract(totalProductDiscountAmount);
            BigDecimal totalTaxAmount = totalTaxAmount();
            int skuCount = skuCount();

            BigDecimal totalAmount = totalLineItemAmount.add(totalTaxAmount)
                    .subtract(totalProductDiscountAmount)
                    .subtract(orderLevelDiscountAmount);

            return ClinicOrder.builder()
                    .clinicCode(command.clinicCode())
                    .orderNumber(idGenerator.next(PosBusinessIdConstants.CLINIC_ORDER))
                    .customerId(command.customerId())
                    .currency(command.currency())
                    .status(ClinicOrderStatusEnum.UNPAID)
                    .totalAmount(totalAmount)
                    .currentTotalAmount(totalAmount)
                    .totalLineItemAmount(totalLineItemAmount)
                    .currentTotalLineItemAmount(totalLineItemAmount)
                    .subtotalAmount(subtotalAmount)
                    .currentSubtotalAmount(subtotalAmount)
                    .totalTaxAmount(totalTaxAmount)
                    .currentTotalTaxAmount(totalTaxAmount)
                    .orderLevelDiscountAmount(orderLevelDiscountAmount)
                    .currentOrderLevelDiscountAmount(orderLevelDiscountAmount)
                    .totalProductDiscountAmount(totalProductDiscountAmount)
                    .currentTotalProductDiscountAmount(totalProductDiscountAmount)
                    .skuCount(skuCount)
                    .currentSkuCount(skuCount)
                    .note(command.note())
                    .saleUserCode(command.saleUserCode())
                    .paymentChannel(null)
                    .paymentMethod(null)
                    .paidAt(null)
                    .build();
        }

        private BigDecimal totalLineItemAmount() {
            // TODO
            return BigDecimal.ZERO;
        }

        private BigDecimal totalProductDiscountAmount() {
            // TODO
            return BigDecimal.ZERO;
        }

        private BigDecimal orderLevelDiscountAmount() {
            // TODO
            return BigDecimal.ZERO;
        }

        private BigDecimal totalTaxAmount() {
            // TODO
            return BigDecimal.ZERO;
        }

        private int skuCount() {
            return CollectionUtils.safeStream(command.products())
                    .mapToInt(CreateClinicOrderCommand.Product::quantity)
                    .sum();
        }

        private List<ClinicOrderLineItem> buildClinicOrderLineItems() {
            // TODO
            return List.of();
        }

    }

}
