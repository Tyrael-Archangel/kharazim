package com.tyrael.kharazim.application.customer.converter;

import com.tyrael.kharazim.application.config.DictCodeConstants;
import com.tyrael.kharazim.application.customer.domain.Customer;
import com.tyrael.kharazim.application.customer.vo.CustomerBaseVO;
import com.tyrael.kharazim.application.system.service.DictService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
@Component
@RequiredArgsConstructor
public class CustomerConverter {

    private final DictService dictService;

    /**
     * Customer -> CustomerBaseVO
     */
    public CustomerBaseVO customerBaseVO(Customer customer) {
        String sourceChannelDict = customer.getSourceChannelDict();
        String sourceChannel = StringUtils.isNotBlank(sourceChannelDict)
                ? dictService.findItemName(DictCodeConstants.CUSTOMER_SOURCE_CHANNEL, sourceChannelDict)
                : null;
        return CustomerBaseVO.builder()
                .code(customer.getCode())
                .name(customer.getName())
                .gender(customer.getGender())
                .birthYear(customer.getBirthYear())
                .birthMonth(customer.getBirthMonth())
                .birthDayOfMonth(customer.getBirthDayOfMonth())
                .phone(customer.getPhone())
                .phoneVerified(customer.getPhoneVerified())
                .certificateType(customer.getCertificateType())
                .certificateCode(customer.getCertificateCode())
                .wechatCode(customer.getWechatCode())
                .wechatName(customer.getWechatName())
                .remark(customer.getRemark())
                .sourceChannelDictValue(sourceChannelDict)
                .sourceChannel(sourceChannel)
                .build();
    }

}
