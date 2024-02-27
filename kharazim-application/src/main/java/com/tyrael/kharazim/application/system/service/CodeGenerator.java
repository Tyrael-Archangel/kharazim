package com.tyrael.kharazim.application.system.service;


import com.tyrael.kharazim.application.config.BusinessCodeConstants;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
public interface CodeGenerator {

    /**
     * 获取指定位数的编码
     *
     * @param businessCode 业务编码定义
     * @return 编码
     */
    String next(BusinessCodeConstants businessCode);

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
    String dailyNext(BusinessCodeConstants businessCode);

    /**
     * 按日期时间获取
     *
     * @param businessCode 业务编码定义
     * @return 包含日期时间的编码
     */
    String dailyTimeNext(BusinessCodeConstants businessCode);

}
