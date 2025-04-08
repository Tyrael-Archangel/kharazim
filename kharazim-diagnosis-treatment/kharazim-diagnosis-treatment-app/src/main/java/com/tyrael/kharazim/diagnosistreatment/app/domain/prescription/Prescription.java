package com.tyrael.kharazim.diagnosistreatment.app.domain.prescription;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.diagnosistreatment.app.enums.PrescriptionCreateStatus;
import com.tyrael.kharazim.mybatis.BaseDO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 处方
 *
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
@Data
public class Prescription extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 处方编码
     */
    private String code;

    /**
     * 会员编码
     */
    private String customerCode;

    /**
     * 诊所（机构）编码
     */
    private String clinicCode;

    /**
     * 总金额（元）
     */
    private BigDecimal totalAmount;

    /**
     * 商品明细
     */
    @TableField(exist = false)
    private List<PrescriptionProduct> products;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建状态
     */
    private PrescriptionCreateStatus createStatus;

    /**
     * 支付成功时间
     */
    private LocalDateTime paidTime;

    /**
     * 是否处于创建中
     */
    public boolean isCreating() {
        return PrescriptionCreateStatus.CREATING.equals(createStatus);
    }

    public void markCreateSuccess() {
        this.createStatus = PrescriptionCreateStatus.CREATE_SUCCESS;
    }

    public void markCreateFailed() {
        this.createStatus = PrescriptionCreateStatus.CREATE_FAILED;
    }

    public void markPaid() {
        this.paidTime = LocalDateTime.now();
    }

}
