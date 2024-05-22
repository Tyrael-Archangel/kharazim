package com.tyrael.kharazim.application.demo.calculator;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2022/2/9
 */
public enum SequenceGroup {

    /**
     * 括号
     */
    BRACKET,

    /**
     * 百分比
     */
    PERCENT,

    /**
     * 三角函数
     */
    TRIGONOMETRIC,

    /**
     * 乘、除
     */
    MULTIPLY_DIVIDE,

    /**
     * 加、减
     */
    ADD_SUBTRACT;

    public static List<SequenceGroup> getSequences() {
        return List.of(BRACKET, PERCENT, TRIGONOMETRIC, MULTIPLY_DIVIDE, ADD_SUBTRACT);
    }

}
