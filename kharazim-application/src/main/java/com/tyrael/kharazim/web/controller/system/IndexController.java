package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.common.dto.DataResponse;
import com.tyrael.kharazim.common.dto.Response;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
@Hidden
@RestController
public class IndexController {

    @RequestMapping("/")
    public Response index() {
        return DataResponse.ok("Welcome!");
    }

}
