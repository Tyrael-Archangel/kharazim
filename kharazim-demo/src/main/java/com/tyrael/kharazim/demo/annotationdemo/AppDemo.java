package com.tyrael.kharazim.demo.annotationdemo;

import java.lang.reflect.Field;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
public class AppDemo {

    public static void main(String[] args) throws Exception {
        DemoDTO demoDTO = new DemoDTO();
        Class<? extends DemoDTO> clazz = demoDTO.getClass();

        Field field = clazz.getDeclaredField("field");
        MyAnnotation annotation = field.getAnnotation(MyAnnotation.class);
        Class<? extends DemoConverter> converterClass = annotation.converter();
        DemoConverter converter = converterClass.getDeclaredConstructor().newInstance();
        System.out.println("filed 上注解的值： " + converter.getName());

    }

}
