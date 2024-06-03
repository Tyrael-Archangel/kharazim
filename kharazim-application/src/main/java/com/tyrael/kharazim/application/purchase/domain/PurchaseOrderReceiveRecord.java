package com.tyrael.kharazim.application.purchase.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/5/31
 */
@Data
public class PurchaseOrderReceiveRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 流水号
     */
    private String serialCode;

    /**
     * 采购单号
     */
    private String purchaseOrderCode;

    /**
     * 物流跟踪号
     */
    private String trackingNumber;

    /**
     * 收货时间
     */
    private LocalDateTime receiveTime;

    /**
     * 收货人
     */
    private String receiveUser;

    /**
     * 收货人编码
     */
    private String receiveUserCode;

    @TableField(exist = false)
    private List<PurchaseOrderReceiveRecordItem> recordItems;

}
