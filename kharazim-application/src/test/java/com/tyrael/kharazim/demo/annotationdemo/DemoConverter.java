package com.tyrael.kharazim.demo.annotationdemo;

import java.util.UUID;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
public class DemoConverter {

    private final String abc;

    public DemoConverter() {
        this.abc = "abc";
    }

    public String getName() {
        return abc + "===" + UUID.randomUUID();
    }

}
