package com.tyrael.kharazim.application.customer.converter;

import com.tyrael.kharazim.application.config.DictCodeConstants;
import com.tyrael.kharazim.application.customer.domain.*;
import com.tyrael.kharazim.application.customer.vo.customer.*;
import com.tyrael.kharazim.application.system.service.DictService;
import com.tyrael.kharazim.application.system.service.FileService;
import com.tyrael.kharazim.application.user.domain.User;
import com.tyrael.kharazim.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
@Component
@RequiredArgsConstructor
public class CustomerConverter {

    private final DictService dictService;
    private final FileService fileService;

    /**
     * Customers -> CustomerBaseVOs
     */
    public List<CustomerBaseVO> customerBaseVOs(Collection<Customer> customers) {
        if (customers == null || customers.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, String> sourceChannelValueToName = dictService.dictItemMap(
                DictCodeConstants.CUSTOMER_SOURCE_CHANNEL);

        return customers.stream()
                .map(customer -> {
                    String sourceChannelDict = customer.getSourceChannelDict();
                    String sourceChannel = sourceChannelValueToName.get(sourceChannelDict);
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
                            .createTime(customer.getCreateTime())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * Customer -> CustomerBaseVO
     */
    public CustomerBaseVO customerBaseVO(Customer customer) {
        return this.customerBaseVOs(Collections.singleton(customer))
                .iterator().next();
    }

    /**
     * Customer -> CustomerSimpleVO
     */
    public CustomerSimpleVO customerSimpleVO(Customer customer) {
        return CustomerSimpleVO.builder()
                .code(customer.getCode())
                .name(customer.getName())
                .phone(customer.getPhone())
                .certificateType(customer.getCertificateType())
                .certificateCode(customer.getCertificateCode())
                .build();
    }

    /**
     * CustomerAddress -> CustomerAddressVO
     */
    public CustomerAddressVO customerAddressVO(CustomerAddress customerAddress) {
        return CustomerAddressVO.builder()
                .customerAddressId(customerAddress.getId())
                .contact(customerAddress.getContact())
                .contactPhone(customerAddress.getContactPhone())
                .provinceCode(customerAddress.getProvinceCode())
                .provinceName(customerAddress.getProvinceName())
                .cityCode(customerAddress.getCityCode())
                .cityName(customerAddress.getCityName())
                .countyCode(customerAddress.getCountyCode())
                .countyName(customerAddress.getCountyName())
                .detailAddress(customerAddress.getDetailAddress())
                .defaultAddress(Boolean.TRUE.equals(customerAddress.getDefaultAddress()))
                .build();
    }

    /**
     * CustomerInsurance -> CustomerInsuranceVO
     */
    public CustomerInsuranceVO customerInsuranceVO(CustomerInsurance customerInsurance) {

        String companyDict = customerInsurance.getCompanyDict();
        String companyName = dictService.findItemName(DictCodeConstants.INSURANCE_COMPANY, companyDict);

        return CustomerInsuranceVO.builder()
                .customerInsuranceId(customerInsurance.getId())
                .companyName(companyName)
                .companyDictValue(companyDict)
                .policyNumber(customerInsurance.getPolicyNumber())
                .duration(customerInsurance.getDuration())
                .benefits(customerInsurance.getBenefits())
                .defaultInsurance(customerInsurance.getDefaultInsurance())
                .build();
    }


    /**
     * CustomerServiceUser、User -> CustomerServiceUserVO
     */
    public CustomerServiceUserVO customerServiceUserVO(CustomerServiceUser customerServiceUser, User user) {
        return CustomerServiceUserVO.builder()
                .customerCode(customerServiceUser.getCustomerCode())
                .serviceUserCode(customerServiceUser.getServiceUserCode())
                .serviceUserName(user.getNickName())
                .serviceUserAvatar(user.getAvatar())
                .serviceUserAvatarUrl(fileService.getUrl(user.getAvatar()))
                .updateTime(customerServiceUser.getUpdateTime())
                .build();
    }

    /**
     * CustomerSalesConsultant、User -> CustomerSalesConsultantVO
     */
    public CustomerSalesConsultantVO customerSalesConsultantVO(CustomerSalesConsultant customerSalesConsultant, User user) {
        return CustomerSalesConsultantVO.builder()
                .customerCode(customerSalesConsultant.getCustomerCode())
                .salesConsultantCode(customerSalesConsultant.getSalesConsultantCode())
                .salesConsultantName(user.getNickName())
                .salesConsultantAvatar(user.getAvatar())
                .salesConsultantAvatarUrl(fileService.getUrl(user.getAvatar()))
                .updateTime(customerSalesConsultant.getUpdateTime())
                .build();
    }

    /**
     * Customers -> CustomerExportVOs
     */
    public List<CustomerExportVO> customerExportVOs(Collection<Customer> customers) {
        if (customers == null || customers.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, String> sourceChannelValueToName = dictService.dictItemMap(
                DictCodeConstants.CUSTOMER_SOURCE_CHANNEL);

        return customers.stream()
                .map(customer -> CustomerExportVO.builder()
                        .code(customer.getCode())
                        .name(customer.getName())
                        .gender(customer.getGender())
                        .birthday(customerBirthday(customer))
                        .phone(customer.getPhone())
                        .certificateType(customer.getCertificateType())
                        .certificateCode(customer.getCertificateCode())
                        .remark(customer.getRemark())
                        .sourceChannel(sourceChannelValueToName.get(customer.getSourceChannelDict()))
                        .createTime(customer.getCreateTime())
                        .build()
                )
                .collect(Collectors.toList());
    }

    private String customerBirthday(Customer customer) {
        Integer birthYear = customer.getBirthYear();
        Integer birthDayOfMonth = customer.getBirthDayOfMonth();
        Integer birthMonth = customer.getBirthMonth();

        if (birthMonth != null && birthDayOfMonth != null) {
            try {
                if (birthYear != null) {
                    return LocalDate.of(birthYear, birthMonth, birthDayOfMonth).toString();
                } else {
                    return MonthDay.of(birthMonth, birthDayOfMonth).toString();
                }
            } catch (DateTimeException e) {
                throw new BusinessException(e.getMessage(), e);
            }
        }
        return null;
    }

}
