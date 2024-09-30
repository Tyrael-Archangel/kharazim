package com.tyrael.kharazim;

import com.tyrael.kharazim.web.controller.clinic.AddClinicTest;
import com.tyrael.kharazim.web.controller.customer.AddCustomerTest;
import com.tyrael.kharazim.web.controller.pharmacy.InboundTest;
import com.tyrael.kharazim.web.controller.pharmacy.OutboundTest;
import com.tyrael.kharazim.web.controller.prescription.AddPrescriptionTest;
import com.tyrael.kharazim.web.controller.product.AddProductCategoryTest;
import com.tyrael.kharazim.web.controller.product.AddProductSkuTest;
import com.tyrael.kharazim.web.controller.product.AddProductUnitTest;
import com.tyrael.kharazim.web.controller.product.PublishSkuTest;
import com.tyrael.kharazim.web.controller.purchase.AddPurchaseOrderTest;
import com.tyrael.kharazim.web.controller.recharge.AddCustomerRechargeCardTest;
import com.tyrael.kharazim.web.controller.recharge.AddRechargeCardTypeTest;
import com.tyrael.kharazim.web.controller.settlement.PaySettlementOrderTest;
import com.tyrael.kharazim.web.controller.supplier.AddSupplierTest;
import com.tyrael.kharazim.web.controller.user.AddRoleTest;
import com.tyrael.kharazim.web.controller.user.AddUserTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Tyrael Archangel
 * @since 2024/9/30
 */
@Suite
@SelectClasses({
        // 注意顺序，测试类会按此处定义的顺序依次执行

        AddRoleTest.class,
        AddUserTest.class,
        AddClinicTest.class,
        AddSupplierTest.class,
        AddCustomerTest.class,
        AddRechargeCardTypeTest.class,
        AddProductUnitTest.class,
        AddProductCategoryTest.class,
        AddProductSkuTest.class,

        AddPurchaseOrderTest.class,
        InboundTest.class,

        PublishSkuTest.class,
        AddPrescriptionTest.class,
        AddCustomerRechargeCardTest.class,
        PaySettlementOrderTest.class,

        OutboundTest.class,
})
public class GenerateDataTest {
}
