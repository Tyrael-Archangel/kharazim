package com.tyrael.kharazim.application.clinic.service;

import com.tyrael.kharazim.application.clinic.vo.PageClinicRequest;
import com.tyrael.kharazim.mock.MockFileHttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @author Tyrael Archangel
 * @since 2024/5/24
 */
@SpringBootTest
class ClinicServiceTest {

    @Autowired
    private ClinicService clinicService;

    @Test
    void export() throws IOException {
        String fileName = "诊所数据" + System.currentTimeMillis() + ".xlsx";
        clinicService.export(new PageClinicRequest(), new MockFileHttpServletResponse(fileName));
    }

}