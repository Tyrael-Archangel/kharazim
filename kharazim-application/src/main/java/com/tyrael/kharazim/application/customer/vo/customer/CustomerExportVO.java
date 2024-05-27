package com.tyrael.kharazim.application.customer.vo.customer;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.tyrael.kharazim.application.user.enums.UserCertificateTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/5/22
 */
@Data
@Builder
@ExcelIgnoreUnannotated
@ColumnWidth(16)
public class CustomerExportVO {

    @ExcelProperty({"会员基本信息", "会员编号"})
    private String code;

    @ExcelProperty({"会员基本信息", "会员姓名"})
    private String name;

    @ExcelProperty("会员性别")
    private UserGenderEnum gender;

    @ExcelProperty("生日")
    @ColumnWidth(14)
    private String birthday;

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("证件类型")
    private UserCertificateTypeEnum certificateType;

    @ExcelProperty("证件号码")
    @ColumnWidth(20)
    private String certificateCode;

    @ExcelProperty("会员来源渠道")
    @ColumnWidth(18)
    private String sourceChannel;

    @ExcelProperty("创建时间")
    @ColumnWidth(18)
    private LocalDateTime createTime;

    @ExcelProperty("备注")
    @ColumnWidth(25)
    private String remark;

}
