package com.tyrael.kharazim.idgenerator;

/**
 * @author Tyrael Archangel
 * @since 2025/2/18
 */
@SuppressWarnings("unused")
public interface BusinessIdConstant<E extends Enum<E> & BusinessIdConstant<E>> {

    int DEFAULT_BIT = 4;

    /**
     * prefix
     *
     * @return prefix
     */
    String getPrefix();

    /**
     * desc
     *
     * @return desc
     */
    String getDesc();

    /**
     * bit
     *
     * @return bit
     */
    default int getBit() {
        return DEFAULT_BIT;
    }

}
