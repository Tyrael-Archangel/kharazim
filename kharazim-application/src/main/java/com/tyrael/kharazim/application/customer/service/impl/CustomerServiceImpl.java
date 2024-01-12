package com.tyrael.kharazim.application.customer.service.impl;

import com.google.common.collect.Lists;
import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.config.DictCodeConstants;
import com.tyrael.kharazim.application.customer.converter.CustomerConverter;
import com.tyrael.kharazim.application.customer.domain.*;
import com.tyrael.kharazim.application.customer.mapper.*;
import com.tyrael.kharazim.application.customer.service.CustomerService;
import com.tyrael.kharazim.application.customer.vo.*;
import com.tyrael.kharazim.application.system.service.CaptchaService;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.application.system.service.DictService;
import com.tyrael.kharazim.application.user.mapper.UserMapper;
import com.tyrael.kharazim.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CodeGenerator codeGenerator;
    private final CustomerConverter customerConverter;
    private final CustomerMapper customerMapper;
    private final CustomerAddressMapper customerAddressMapper;
    private final CustomerInsuranceMapper customerInsuranceMapper;
    private final CustomerServiceUserMapper customerServiceUserMapper;
    private final CustomerSalesConsultantMapper customerSalesConsultantMapper;
    private final UserMapper userMapper;
    private final DictService dictService;
    private final CaptchaService captchaService;

    @Override
    public CustomerBaseVO findByCode(String code) {
        Customer customer = customerMapper.exactlyFindByCode(code);
        return customerConverter.customerBaseVO(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(AddCustomerRequest addCustomerRequest, AuthUser currentUser) {

        Customer customer = buildCustomer(addCustomerRequest);
        customerMapper.insert(customer);

        String customerCode = customer.getCode();

        // 专属客服
        String serviceUserCode = addCustomerRequest.getServiceUserCode();
        if (StringUtils.isNotBlank(serviceUserCode)) {
            this.assignCustomerServiceUser(customerCode, serviceUserCode, currentUser);
        }

        // 专属销售顾问
        String salesConsultantCode = addCustomerRequest.getSalesConsultantCode();
        if (StringUtils.isNotBlank(salesConsultantCode)) {
            this.assignCustomerSalesConsultant(customerCode, salesConsultantCode, currentUser);
        }

        // 会员地址
        List<AddCustomerAddressRequest> customerAddresses = addCustomerRequest.getCustomerAddresses();
        if (!CollectionUtils.isEmpty(customerAddresses)) {
            for (AddCustomerAddressRequest customerAddress : customerAddresses) {
                customerAddress.setCustomerCode(customerCode);
                this.addAddress(customerAddress);
            }
        }

        // 会员保险
        List<AddCustomerInsuranceRequest> customerInsurances = addCustomerRequest.getCustomerInsurances();
        if (!CollectionUtils.isEmpty(customerInsurances)) {
            for (AddCustomerInsuranceRequest customerInsurance : customerInsurances) {
                customerInsurance.setCustomerCode(customerCode);
                this.addInsurance(customerInsurance);
            }
        }

        return customerCode;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(String code, ModifyCustomerRequest modifyCustomerRequest, AuthUser currentUser) {
        Customer customer = customerMapper.exactlyFindByCode(code);

        Integer birthYear = modifyCustomerRequest.getBirthYear();
        Integer birthMonth = modifyCustomerRequest.getBirthMonth();
        Integer birthDayOfMonth = modifyCustomerRequest.getBirthDayOfMonth();
        checkBirthday(birthYear, birthMonth, birthDayOfMonth);

        customer.setName(modifyCustomerRequest.getName());
        customer.setGender(modifyCustomerRequest.getGender());
        customer.setBirthYear(birthYear);
        customer.setBirthMonth(birthMonth);
        customer.setBirthDayOfMonth(birthDayOfMonth);
        String originalPhone = customer.getPhone();
        String phone = modifyCustomerRequest.getPhone();
        customer.setPhone(phone);
        if (!StringUtils.equals(originalPhone, phone)) {
            customer.setPhoneVerified(false);
        }
        customer.setCertificateType(modifyCustomerRequest.getCertificateType());
        customer.setCertificateCode(modifyCustomerRequest.getCertificateCode());
        customer.setRemark(modifyCustomerRequest.getRemark());

        customer.setUpdate(currentUser.getCode(), currentUser.getNickName());
        customerMapper.updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifySource(ModifyCustomerSourceRequest modifySourceRequest, AuthUser currentUser) {
        String customerCode = modifySourceRequest.getCustomerCode();
        Customer customer = customerMapper.exactlyFindByCode(customerCode);

        String sourceCustomerCode = modifySourceRequest.getSourceCustomerCode();
        customerMapper.ensureCustomerExist(sourceCustomerCode);

        ensureCustomerSourceNotCircular(customer, sourceCustomerCode);

        customer.setSourceCustomerCode(sourceCustomerCode);
        customer.setUpdate(currentUser.getCode(), currentUser.getNickName());
        customerMapper.updateById(customer);
    }

    /**
     * 验证会员的来源会员不能形成循环
     */
    private void ensureCustomerSourceNotCircular(Customer customer, String sourceCustomerCode) {
        BusinessException.assertTrue(!StringUtils.equals(customer.getCode(), sourceCustomerCode),
                "会员的来源会员不能选择自己");

        LinkedHashMap<String, Customer> customerLink = new LinkedHashMap<>();
        customerLink.put(customer.getCode(), customer);

        String tempSourceCustomerCode = sourceCustomerCode;
        while (true) {
            Customer sourceCustomer = customerMapper.findByCode(tempSourceCustomerCode);
            if (sourceCustomer == null) {
                return;
            }
            String sourceSourceCustomerCode = sourceCustomer.getSourceCustomerCode();
            if (StringUtils.isEmpty(sourceSourceCustomerCode)) {
                return;
            }

            customerLink.put(sourceCustomer.getCode(), sourceCustomer);
            tempSourceCustomerCode = sourceSourceCustomerCode;

            if (customerLink.containsKey(sourceSourceCustomerCode)) {
                LinkedList<String> circulation = customerLink.values()
                        .stream()
                        .map(c -> c.getName() + "(" + c.getCode() + ")")
                        .collect(Collectors.toCollection(LinkedList::new));
                circulation.add(customer.getName() + "(" + customer.getCode() + ")");
                String circulationString = String.join(" -> ", Lists.reverse(circulation));
                throw new BusinessException("会员的来源会员关系形成循环：" + circulationString);
            }
        }
    }

    private Customer buildCustomer(AddCustomerRequest request) {
        String sourceCustomerCode = request.getSourceCustomerCode();
        if (StringUtils.isNotBlank(sourceCustomerCode)) {
            customerMapper.ensureCustomerExist(sourceCustomerCode);
        }

        String sourceChannelDict = request.getSourceChannelDictValue();
        if (StringUtils.isNotBlank(sourceChannelDict)) {
            dictService.ensureDictItemEnable(DictCodeConstants.CUSTOMER_SOURCE_CHANNEL, sourceChannelDict);
        }

        checkBirthday(request.getBirthYear(), request.getBirthMonth(), request.getBirthDayOfMonth());
        boolean phoneVerified = this.verifyCaptcha(request.getPhone(), request.getCaptcha(), request.getCaptchaSerialCode());

        String code = codeGenerator.next(BusinessCodeConstants.CUSTOMER);

        Customer customer = new Customer();
        customer.setCode(code);
        customer.setName(request.getName());
        customer.setGender(request.getGender());
        customer.setBirthYear(request.getBirthYear());
        customer.setBirthMonth(request.getBirthMonth());
        customer.setBirthDayOfMonth(request.getBirthDayOfMonth());
        customer.setPhone(request.getPhone());
        customer.setPhoneVerified(phoneVerified);
        customer.setCertificateType(request.getCertificateType());
        customer.setCertificateCode(request.getCertificateCode());
        customer.setSourceChannelDict(sourceChannelDict);
        customer.setSourceCustomerCode(sourceCustomerCode);
        customer.setRemark(request.getRemark());

        return customer;
    }

    private void checkBirthday(Integer birthYear,
                               Integer birthMonth,
                               Integer birthDayOfMonth) {
        if (birthMonth != null && birthDayOfMonth != null) {
            try {
                if (birthYear != null) {
                    LocalDate ignore = LocalDate.of(birthYear, birthMonth, birthDayOfMonth);
                } else {
                    MonthDay ignore = MonthDay.of(birthMonth, birthDayOfMonth);
                }
            } catch (DateTimeException e) {
                throw new BusinessException(e.getMessage(), e);
            }
        }
    }

    private boolean verifyCaptcha(String phone, String captcha, String captchaSerialCode) {
        if (StringUtils.isAnyBlank(phone, captcha, captchaSerialCode)) {
            return false;
        }

        captchaService.verifySmsCaptcha(captcha, captchaSerialCode);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignCustomerServiceUser(String customerCode, String serviceUserCode, AuthUser currentUser) {
        customerMapper.ensureCustomerExist(customerCode);
        userMapper.ensureUserExist(serviceUserCode);

        CustomerServiceUser customerServiceUser = customerServiceUserMapper.findByCustomerCode(customerCode);
        if (customerServiceUser == null) {
            customerServiceUser = new CustomerServiceUser();
            customerServiceUser.setCustomerCode(customerCode);
            customerServiceUser.setServiceUserCode(serviceUserCode);
            customerServiceUser.setDeletedTimestamp(0L);
            customerServiceUserMapper.insert(customerServiceUser);

        } else if (!StringUtils.equals(customerServiceUser.getServiceUserCode(), serviceUserCode)) {
            customerServiceUser.setServiceUserCode(serviceUserCode);
            customerServiceUser.setUpdate(currentUser.getCode(), currentUser.getNickName());
            customerServiceUserMapper.updateById(customerServiceUser);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignCustomerSalesConsultant(String customerCode, String salesConsultantCode, AuthUser currentUser) {
        customerMapper.ensureCustomerExist(customerCode);
        userMapper.ensureUserExist(salesConsultantCode);

        CustomerSalesConsultant customerSalesConsultant = customerSalesConsultantMapper.findByCustomerCode(customerCode);
        if (customerSalesConsultant == null) {
            customerSalesConsultant = new CustomerSalesConsultant();
            customerSalesConsultant.setCustomerCode(customerCode);
            customerSalesConsultant.setSalesConsultantCode(salesConsultantCode);
            customerSalesConsultant.setDeletedTimestamp(0L);
            customerSalesConsultantMapper.insert(customerSalesConsultant);

        } else if (!StringUtils.equals(customerSalesConsultant.getSalesConsultantCode(), salesConsultantCode)) {
            customerSalesConsultant.setSalesConsultantCode(salesConsultantCode);
            customerSalesConsultant.setUpdate(currentUser.getCode(), currentUser.getNickName());
            customerSalesConsultantMapper.updateById(customerSalesConsultant);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addAddress(AddCustomerAddressRequest addCustomerAddressRequest) {
        String customerCode = addCustomerAddressRequest.getCustomerCode();
        customerMapper.ensureCustomerExist(customerCode);

        CustomerAddress customerAddress = buildCustomerAddress(addCustomerAddressRequest);
        customerAddressMapper.insert(customerAddress);

        if (addCustomerAddressRequest.isCustomerDefaultAddress()) {
            customerAddressMapper.markAddressDefault(customerCode, customerAddress.getId());
        } else {
            // 如果会员只有唯一的地址，则设置为默认地址
            List<CustomerAddress> customerAddresses = customerAddressMapper.listByCustomerCode(customerCode);
            if (customerAddresses.size() == 1) {
                customerAddressMapper.markAddressDefault(customerCode, customerAddress.getId());
            }
        }

        return customerAddress.getId();
    }

    private CustomerAddress buildCustomerAddress(AddCustomerAddressRequest addCustomerAddressRequest) {
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setCustomerCode(addCustomerAddressRequest.getCustomerCode());
        customerAddress.setProvinceCode(addCustomerAddressRequest.getProvinceCode());
        customerAddress.setProvinceName(addCustomerAddressRequest.getProvinceName());
        customerAddress.setContact(addCustomerAddressRequest.getContact());
        customerAddress.setContactPhone(addCustomerAddressRequest.getContactPhone());
        customerAddress.setCityCode(addCustomerAddressRequest.getCityCode());
        customerAddress.setCityName(addCustomerAddressRequest.getCityName());
        customerAddress.setCountyCode(addCustomerAddressRequest.getCountyCode());
        customerAddress.setCountyName(addCustomerAddressRequest.getCountyName());
        customerAddress.setDetailAddress(addCustomerAddressRequest.getDetailAddress());
        customerAddress.setDefaultAddress(false);
        return customerAddress;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addInsurance(AddCustomerInsuranceRequest addCustomerInsuranceRequest) {
        String customerCode = addCustomerInsuranceRequest.getCustomerCode();
        customerMapper.ensureCustomerExist(customerCode);

        CustomerInsurance customerInsurance = createCustomerInsurance(addCustomerInsuranceRequest);
        try {
            customerInsuranceMapper.insert(customerInsurance);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("保单号已存在", e);
        }

        if (addCustomerInsuranceRequest.isCustomerDefaultInsurance()) {
            customerInsuranceMapper.markInsuranceDefault(customerCode, customerInsurance.getId());
        } else {
            // 如果会员只有唯一的保险，则设置为默认保险
            List<CustomerInsurance> customerInsurances = customerInsuranceMapper.listByCustomerCode(customerCode);
            if (customerInsurances.size() == 1) {
                customerInsuranceMapper.markInsuranceDefault(customerCode, customerInsurance.getId());
            }
        }

        return customerInsurance.getId();
    }

    private CustomerInsurance createCustomerInsurance(AddCustomerInsuranceRequest addCustomerInsuranceRequest) {
        String insuranceCompany = addCustomerInsuranceRequest.getCompanyDictValue();
        dictService.ensureDictItemEnable(DictCodeConstants.INSURANCE_COMPANY, insuranceCompany);

        CustomerInsurance customerInsurance = new CustomerInsurance();
        customerInsurance.setCustomerCode(addCustomerInsuranceRequest.getCustomerCode());
        customerInsurance.setCompanyDict(insuranceCompany);
        customerInsurance.setPolicyNumber(addCustomerInsuranceRequest.getPolicyNumber());
        customerInsurance.setDuration(addCustomerInsuranceRequest.getDuration());
        customerInsurance.setBenefits(addCustomerInsuranceRequest.getBenefits());
        customerInsurance.setDeletedTimestamp(0L);
        return customerInsurance;
    }

}
