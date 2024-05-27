package com.tyrael.kharazim.application.prescription.service;

import com.tyrael.kharazim.application.prescription.vo.PagePrescriptionRequest;
import com.tyrael.kharazim.mock.MockFileHttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @author Tyrael Archangel
 * @since 2024/5/27
 */
@SpringBootTest
class PrescriptionServiceTest {

    @Autowired
    private PrescriptionService prescriptionService;

    @Test
    void export() throws IOException {
        String fileName = "处方数据" + System.currentTimeMillis() + ".xlsx";
        prescriptionService.export(new PagePrescriptionRequest(), new MockFileHttpServletResponse(fileName));
    }

}