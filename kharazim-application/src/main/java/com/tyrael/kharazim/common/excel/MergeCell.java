package com.tyrael.kharazim.common.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Tyrael Archangel
 * @since 2024/5/24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MergeCell {

    /**
     * 是否是唯一标识列，如果是，其余需要合并的单元格将会按照该列的合并规则合并
     *
     * @return 是否是唯一标识列
     */
    boolean unique() default false;

}
