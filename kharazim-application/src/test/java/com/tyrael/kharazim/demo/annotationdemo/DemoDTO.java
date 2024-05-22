package com.tyrael.kharazim.demo.annotationdemo;

import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
@Data
public class DemoDTO {

    @MyAnnotation(converter = DemoConverter.class)
    public String field;

}
