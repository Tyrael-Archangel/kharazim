package com.tyrael.kharazim.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tyrael Archangel
 * @since 2025/2/12
 */
@RestController
public class IndexController {

    @RequestMapping
    public String index() {
        return "Welcome!";
    }

}
