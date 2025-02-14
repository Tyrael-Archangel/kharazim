package com.tyrael.kharazim.dubbo.support;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Tyrael Archangel
 * @since 2025/2/13
 */
@Data
public class SourceDubboApplication implements Serializable {

    public static final SourceDubboApplication UNKNOWN_APPLICATION = new UnknownSourceDubboApplication();

    protected String name;
    protected String group;

    private static class UnknownSourceDubboApplication extends SourceDubboApplication {

        private static final String UNKNOWN = "unknown";

        private UnknownSourceDubboApplication() {
            super();
            super.name = UNKNOWN;
            super.group = UNKNOWN;
        }

    }

}
