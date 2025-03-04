package com.tyrael.kharazim.basicdata.app.domain.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
@Data
public class CustomerCommunicationLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 沟通类型字典值
     */
    private String typeDict;

    /**
     * 会员编码
     */
    private String customerCode;

    /**
     * 客服人员编码
     */
    private String serviceUserCode;

    /**
     * 沟通内容
     */
    private String content;

    /**
     * 沟通评价字典值
     */
    private String evaluateDict;

    /**
     * 沟通时间
     */
    private LocalDateTime communicationTime;

    private String creator;
    private String creatorCode;
    private LocalDateTime createTime;

}
