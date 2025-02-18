package com.tyrael.kharazim.idgenerator;

/**
 * @author Tyrael Archangel
 * @since 2025/2/18
 */
public interface IdGenerator {

    /**
     * 获取指定位数的编码
     *
     * @param businessCode 业务编码定义
     * @return 编码
     */
    <E extends Enum<E> & BusinessIdConstant<E>> String next(E businessCode);

    /**
     * 获取指定位数的编码
     *
     * @param tag tag
     * @param bit 位数
     * @return 编码
     */
    String next(String tag, int bit);

    /**
     * 按日期获取
     *
     * @param businessCode 业务编码定义
     * @return 包含日期的编码
     */
    <E extends Enum<E> & BusinessIdConstant<E>> String dailyNext(E businessCode);

    /**
     * 按日期时间获取
     *
     * @param businessCode 业务编码定义
     * @return 包含日期时间的编码
     */
    <E extends Enum<E> & BusinessIdConstant<E>> String dailyTimeNext(E businessCode);

}
