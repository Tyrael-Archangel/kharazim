package com.tyrael.kharazim.lib.base.dto;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
@SuppressWarnings("unused")
public interface BaseHasNameEnum<E extends Enum<E> & BaseHasNameEnum<E>> {

    /**
     * get name
     *
     * @return name
     */
    String getName();

}
