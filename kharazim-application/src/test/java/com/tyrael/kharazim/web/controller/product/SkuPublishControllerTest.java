package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.application.skupublish.vo.PageSkuPublishRequest;
import com.tyrael.kharazim.application.skupublish.vo.PublishSkuRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/3/26
 */
class SkuPublishControllerTest extends BaseControllerTest<SkuPublishController> {

    SkuPublishControllerTest() {
        super(SkuPublishController.class);
    }

    @Test
    void page() {
        PageSkuPublishRequest pageRequest = new PageSkuPublishRequest();
        pageRequest.setSkuName("测试");
        super.performWhenCall(mockController.page(pageRequest));
    }

    @Test
    void publish() {
        PublishSkuRequest publishRequest = new PublishSkuRequest();
        publishRequest.setSkuCode("P240808000001");
        publishRequest.setClinicCode("CL000001");
        publishRequest.setPrice(new BigDecimal("69.88"));
        publishRequest.setEffectBegin(LocalDateTime.now());
        publishRequest.setEffectEnd(LocalDateTime.now().plusYears(5));

        super.performWhenCall(mockController.publish(publishRequest));
    }

    @Test
    void cancelPublish() {
        String code = "SPB2024032617114000002";
        super.performWhenCall(mockController.cancelPublish(code, super.mockAdmin()));
    }

}