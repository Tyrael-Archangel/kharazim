package com.tyrael.kharazim.basicdata.app.service.customer;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcelFactory;
import cn.idev.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.app.constant.BasicDataBusinessIdConstants;
import com.tyrael.kharazim.basicdata.app.constant.BasicDataDictConstants;
import com.tyrael.kharazim.basicdata.app.converter.CustomerConverter;
import com.tyrael.kharazim.basicdata.app.domain.customer.*;
import com.tyrael.kharazim.basicdata.app.dto.customer.customer.*;
import com.tyrael.kharazim.basicdata.app.mapper.customer.*;
import com.tyrael.kharazim.basicdata.sdk.service.DictServiceApi;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import com.tyrael.kharazim.user.sdk.model.UserSimpleVO;
import com.tyrael.kharazim.user.sdk.service.UserServiceApi;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerConverter customerConverter;
    private final CustomerMapper customerMapper;
    private final CustomerAddressMapper customerAddressMapper;
    private final CustomerInsuranceMapper customerInsuranceMapper;
    private final CustomerServiceUserMapper customerServiceUserMapper;
    private final CustomerSalesConsultantMapper customerSalesConsultantMapper;
    private final CustomerTagMapper customerTagMapper;
    private final DictServiceApi dictService;
    private final IdGenerator idGenerator;

    @DubboReference
    private UserServiceApi userServiceApi;

    @Override
    public CustomerBaseVO findByCode(String code) {
        Customer customer = customerMapper.findByCode(code);
        if (customer == null) {
            return null;
        }
        return customerConverter.customerBaseVO(customer);
    }

    @Override
    public List<CustomerBaseVO> listByCodes(Collection<String> codes) {
        List<Customer> customers = customerMapper.listByCodes(codes);
        return customerConverter.customerBaseVOs(customers);
    }

    @Override
    public List<CustomerBaseVO> listAll() {
        List<Customer> customers = customerMapper.selectList(new QueryWrapper<>());
        return customerConverter.customerBaseVOs(customers);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CustomerBaseVO> page(PageCustomerRequest pageRequest) {
        Page<Customer> pageCondition = new Page<>(pageRequest.getPageIndex(), pageRequest.getPageSize());
        PageResponse<Customer> pageData = customerMapper.page(pageRequest, pageCondition);
        return PageResponse.success(
                customerConverter.customerBaseVOs(pageData.getData()),
                pageData.getTotalCount());
    }

    @Override
    @Transactional(readOnly = true)
    public void export(PageCustomerRequest pageRequest, HttpServletResponse response) throws IOException {
        WriteSheet writeSheet = FastExcelFactory.writerSheet("会员数据")
                .head(CustomerExportVO.class)
                .build();
        int pageSize = 200;
        int pageNum = 1;
        try (ExcelWriter excelWriter = FastExcelFactory.write(response.getOutputStream()).build()) {
            List<CustomerExportVO> exports;
            do {
                Page<Customer> pageCondition = new Page<>(pageNum, pageSize, false);
                PageResponse<Customer> pageData = customerMapper.page(pageRequest, pageCondition);

                exports = customerConverter.customerExportVOs(pageData.getData());
                excelWriter.write(exports, writeSheet);

                pageNum++;
            } while (!exports.isEmpty());

            ContentDisposition contentDisposition = ContentDisposition.attachment()
                    .filename("会员数据.xlsx", StandardCharsets.UTF_8)
                    .build();
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        }
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
        if (CollectionUtils.isNotEmpty(customerAddresses)) {
            for (AddCustomerAddressRequest customerAddress : customerAddresses) {
                customerAddress.setCustomerCode(customerCode);
                this.addAddress(customerAddress);
            }
        }

        // 会员保险
        List<AddCustomerInsuranceRequest> customerInsurances = addCustomerRequest.getCustomerInsurances();
        if (CollectionUtils.isNotEmpty(customerInsurances)) {
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

        customer.setUpdateUser(currentUser.getCode(), currentUser.getNickName());
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
        customer.setUpdateUser(currentUser.getCode(), currentUser.getNickName());
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
                Collections.reverse(circulation);
                String circulationString = String.join(" -> ", circulation);
                throw new BusinessException("会员的来源会员关系形成循环：" + circulationString);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindPhone(BindCustomerPhoneRequest request, AuthUser currentUser) {
        String customerCode = request.getCustomerCode();
        Customer customer = customerMapper.exactlyFindByCode(customerCode);

        customer.setPhone(request.getPhone());
        customer.setPhoneVerified(Boolean.TRUE);
        customer.setUpdateUser(currentUser.getCode(), currentUser.getNickName());
        customerMapper.updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbindPhone(String code, AuthUser currentUser) {
        Customer customer = customerMapper.exactlyFindByCode(code);

        customer.setPhoneVerified(false);
        customer.setUpdateUser(currentUser.getCode(), currentUser.getNickName());
        customerMapper.updateById(customer);
    }

    private Customer buildCustomer(AddCustomerRequest request) {
        String sourceCustomerCode = request.getSourceCustomerCode();
        if (StringUtils.isNotBlank(sourceCustomerCode)) {
            customerMapper.ensureCustomerExist(sourceCustomerCode);
        }

        String sourceChannelDict = request.getSourceChannelDictKey();
        if (StringUtils.isNotBlank(sourceChannelDict)) {
            dictService.ensureDictItemEnable(BasicDataDictConstants.CUSTOMER_SOURCE_CHANNEL, sourceChannelDict);
        }

        checkBirthday(request.getBirthYear(), request.getBirthMonth(), request.getBirthDayOfMonth());
        String code = idGenerator.next(BasicDataBusinessIdConstants.CUSTOMER);

        Customer customer = new Customer();
        customer.setCode(code);
        customer.setName(request.getName());
        customer.setGender(request.getGender());
        customer.setBirthYear(request.getBirthYear());
        customer.setBirthMonth(request.getBirthMonth());
        customer.setBirthDayOfMonth(request.getBirthDayOfMonth());
        customer.setPhone(request.getPhone());
        customer.setPhoneVerified(false);
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

    @Override
    public List<CustomerSimpleVO> listSimpleInfo(ListCustomerRequest request) {
        List<Customer> customers = customerMapper.list(request);
        return customers.stream()
                .map(customerConverter::customerSimpleVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerAddressVO> addresses(String code) {
        customerMapper.ensureCustomerExist(code);
        List<CustomerAddress> customerAddresses = customerAddressMapper.listByCustomerCode(code);
        return customerAddresses.stream()
                .map(customerConverter::customerAddressVO)
                .sorted(Comparator.comparing(CustomerAddressVO::getDefaultAddress).reversed())
                .collect(Collectors.toList());
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
    public void deleteCustomerAddress(String customerCode, Long customerAddressId) {
        CustomerAddress customerAddress = customerAddressMapper.selectById(customerAddressId);
        DomainNotFoundException.assertFound(customerAddress, customerAddressId);

        if (!StringUtils.equals(customerAddress.getCustomerCode(), customerCode)) {
            throw new BusinessException("地址与会员不匹配");
        }

        customerAddressMapper.deleteById(customerAddressId);
        List<CustomerAddress> customerAddresses = customerAddressMapper.listByCustomerCode(customerCode);
        if (customerAddresses.isEmpty()) {
            return;
        }
        boolean hasDefaultAddress = customerAddresses.stream()
                .anyMatch(e -> Boolean.TRUE.equals(e.getDefaultAddress()));
        if (hasDefaultAddress) {
            return;
        }
        customerAddresses.stream()
                .max(Comparator.comparing(CustomerAddress::getUpdateTime))
                .ifPresent(latest -> customerAddressMapper.markAddressDefault(customerCode, latest.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyCustomerAddress(ModifyCustomerAddressRequest modifyCustomerAddressRequest, AuthUser currentUser) {
        Long customerAddressId = modifyCustomerAddressRequest.getCustomerAddressId();
        CustomerAddress customerAddress = customerAddressMapper.selectById(customerAddressId);
        DomainNotFoundException.assertFound(customerAddress, customerAddressId);

        customerAddress.setContact(modifyCustomerAddressRequest.getContact());
        customerAddress.setContactPhone(modifyCustomerAddressRequest.getContactPhone());
        customerAddress.setProvinceCode(modifyCustomerAddressRequest.getProvinceCode());
        customerAddress.setProvinceName(modifyCustomerAddressRequest.getProvinceName());
        customerAddress.setCityCode(modifyCustomerAddressRequest.getCityCode());
        customerAddress.setCityName(modifyCustomerAddressRequest.getCityName());
        customerAddress.setCountyCode(modifyCustomerAddressRequest.getCountyCode());
        customerAddress.setCountyName(modifyCustomerAddressRequest.getCountyName());
        customerAddress.setDetailAddress(modifyCustomerAddressRequest.getDetailAddress());

        customerAddress.setUpdateUser(currentUser.getCode(), currentUser.getNickName());

        customerAddressMapper.updateById(customerAddress);

        if (modifyCustomerAddressRequest.isCustomerDefaultAddress()
                && !Boolean.TRUE.equals(customerAddress.getDefaultAddress())) {
            customerAddressMapper.markAddressDefault(customerAddress.getCustomerCode(), customerAddressId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAddressDefault(String customerCode, Long customerAddressId) {
        customerMapper.ensureCustomerExist(customerCode);
        customerAddressMapper.markAddressDefault(customerCode, customerAddressId);
    }

    @Override
    public List<CustomerInsuranceVO> insurances(String code) {
        customerMapper.ensureCustomerExist(code);
        List<CustomerInsurance> customerInsurances = customerInsuranceMapper.listByCustomerCode(code);
        return customerInsurances.stream()
                .map(customerConverter::customerInsuranceVO)
                .sorted(Comparator.comparing(CustomerInsuranceVO::getDefaultInsurance).reversed()
                        .thenComparing(CustomerInsuranceVO::getCustomerInsuranceId))
                .collect(Collectors.toList());
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
        String insuranceCompany = addCustomerInsuranceRequest.getCompanyDictKey();
        dictService.ensureDictItemEnable(BasicDataDictConstants.INSURANCE_COMPANY, insuranceCompany);

        CustomerInsurance customerInsurance = new CustomerInsurance();
        customerInsurance.setCustomerCode(addCustomerInsuranceRequest.getCustomerCode());
        customerInsurance.setCompanyDict(insuranceCompany);
        customerInsurance.setPolicyNumber(addCustomerInsuranceRequest.getPolicyNumber());
        customerInsurance.setDuration(addCustomerInsuranceRequest.getDuration());
        customerInsurance.setBenefits(addCustomerInsuranceRequest.getBenefits());
        return customerInsurance;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomerInsurance(String customerCode, Long customerInsuranceId) {
        CustomerInsurance customerInsurance = customerInsuranceMapper.selectById(customerInsuranceId);
        DomainNotFoundException.assertFound(customerInsurance, customerInsuranceId);

        if (!StringUtils.equals(customerInsurance.getCustomerCode(), customerCode)) {
            throw new BusinessException("保险与会员不匹配");
        }
        customerInsuranceMapper.deleteById(customerInsuranceId);

        if (Boolean.TRUE.equals(customerInsurance.getDefaultInsurance())) {
            List<CustomerInsurance> customerInsurances = customerInsuranceMapper.listByCustomerCode(customerCode);
            customerInsurances.stream()
                    .min(Comparator.comparing(CustomerInsurance::getId))
                    .ifPresent(first -> customerInsuranceMapper.markInsuranceDefault(customerCode, first.getId()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyCustomerInsurance(ModifyCustomerInsuranceRequest modifyCustomerInsuranceRequest, AuthUser currentUser) {
        Long customerInsuranceId = modifyCustomerInsuranceRequest.getCustomerInsuranceId();
        CustomerInsurance customerInsurance = customerInsuranceMapper.selectById(customerInsuranceId);

        String companyDictKey = modifyCustomerInsuranceRequest.getCompanyDictKey();
        if (!StringUtils.equalsIgnoreCase(customerInsurance.getCompanyDict(), companyDictKey)) {
            dictService.ensureDictItemEnable(BasicDataDictConstants.INSURANCE_COMPANY, companyDictKey);
            customerInsurance.setCompanyDict(companyDictKey);
        }

        customerInsurance.setPolicyNumber(modifyCustomerInsuranceRequest.getPolicyNumber());
        customerInsurance.setDuration(modifyCustomerInsuranceRequest.getDuration());
        customerInsurance.setBenefits(modifyCustomerInsuranceRequest.getBenefits());
        customerInsurance.setUpdateUser(currentUser.getCode(), currentUser.getNickName());

        customerInsuranceMapper.updateById(customerInsurance);

        if (modifyCustomerInsuranceRequest.isCustomerDefaultInsurance()
                && !Boolean.TRUE.equals(customerInsurance.getDefaultInsurance())) {
            this.markInsuranceDefault(customerInsurance.getCustomerCode(), customerInsuranceId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markInsuranceDefault(String customerCode, Long customerInsuranceId) {
        customerMapper.ensureCustomerExist(customerCode);
        customerInsuranceMapper.markInsuranceDefault(customerCode, customerInsuranceId);
    }

    @Override
    public CustomerServiceUserVO customerService(String customerCode) {
        customerMapper.ensureCustomerExist(customerCode);
        CustomerServiceUser customerServiceUser = customerServiceUserMapper.findByCustomerCode(customerCode);
        if (customerServiceUser == null) {
            return null;
        }
        String serviceUserCode = customerServiceUser.getServiceUserCode();
        UserSimpleVO user = userServiceApi.findByCode(serviceUserCode);
        return customerConverter.customerServiceUserVO(customerServiceUser, user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignCustomerServiceUser(String customerCode, String serviceUserCode, AuthUser currentUser) {
        customerMapper.ensureCustomerExist(customerCode);
        userServiceApi.ensureUserExist(serviceUserCode);

        CustomerServiceUser customerServiceUser = customerServiceUserMapper.findByCustomerCode(customerCode);
        if (customerServiceUser == null) {
            customerServiceUser = new CustomerServiceUser();
            customerServiceUser.setCustomerCode(customerCode);
            customerServiceUser.setServiceUserCode(serviceUserCode);
            customerServiceUserMapper.insert(customerServiceUser);

        } else if (!StringUtils.equals(customerServiceUser.getServiceUserCode(), serviceUserCode)) {
            customerServiceUser.setServiceUserCode(serviceUserCode);
            customerServiceUser.setUpdateUser(currentUser.getCode(), currentUser.getNickName());
            customerServiceUserMapper.updateById(customerServiceUser);
        }
    }

    @Override
    public CustomerSalesConsultantVO customerSalesConsultant(String code) {
        customerMapper.ensureCustomerExist(code);

        CustomerSalesConsultant customerSalesConsultant = customerSalesConsultantMapper.findByCustomerCode(code);
        if (customerSalesConsultant == null) {
            return null;
        }
        String salesConsultantCode = customerSalesConsultant.getSalesConsultantCode();
        UserSimpleVO user = userServiceApi.findByCode(salesConsultantCode);
        return customerConverter.customerSalesConsultantVO(customerSalesConsultant, user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignCustomerSalesConsultant(String customerCode, String salesConsultantCode, AuthUser currentUser) {
        customerMapper.ensureCustomerExist(customerCode);
        userServiceApi.ensureUserExist(salesConsultantCode);

        CustomerSalesConsultant customerSalesConsultant = customerSalesConsultantMapper.findByCustomerCode(customerCode);
        if (customerSalesConsultant == null) {
            customerSalesConsultant = new CustomerSalesConsultant();
            customerSalesConsultant.setCustomerCode(customerCode);
            customerSalesConsultant.setSalesConsultantCode(salesConsultantCode);
            customerSalesConsultantMapper.insert(customerSalesConsultant);

        } else if (!StringUtils.equals(customerSalesConsultant.getSalesConsultantCode(), salesConsultantCode)) {
            customerSalesConsultant.setSalesConsultantCode(salesConsultantCode);
            customerSalesConsultant.setUpdateUser(currentUser.getCode(), currentUser.getNickName());
            customerSalesConsultantMapper.updateById(customerSalesConsultant);
        }
    }

    @Override
    public List<CustomerTagVO> customerTags(String code) {
        customerMapper.ensureCustomerExist(code);

        List<CustomerTag> customerTags = customerTagMapper.listByCustomerCode(code);
        if (customerTags.isEmpty()) {
            return new ArrayList<>();
        }
        Map<String, String> dictItemValueToNameMap = dictService.dictItemMap(BasicDataDictConstants.CUSTOMER_TAG);

        return customerTags.stream()
                .map(CustomerTag::getTagDict)
                .map(tagDict -> {
                    CustomerTagVO customerTagVO = new CustomerTagVO();
                    customerTagVO.setTagDictKey(tagDict);
                    customerTagVO.setTagName(dictItemValueToNameMap.get(tagDict));
                    return customerTagVO;
                })
                .filter(e -> StringUtils.isNotBlank(e.getTagName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCustomerTag(AddCustomerTagRequest addCustomerTagRequest, AuthUser currentUser) {
        String customerCode = addCustomerTagRequest.getCustomerCode();
        customerMapper.ensureCustomerExist(customerCode);

        Set<String> tagDictKeys = addCustomerTagRequest.getTagDictKeys();
        dictService.ensureDictItemEnable(BasicDataDictConstants.CUSTOMER_TAG, tagDictKeys);

        for (String tagDict : tagDictKeys) {
            CustomerTag customerTag = new CustomerTag();
            customerTag.setCustomerCode(customerCode);
            customerTag.setTagDict(tagDict);
            try {
                customerTagMapper.insert(customerTag);
            } catch (DuplicateKeyException e) {
                log.warn("会员[{}]标签重复: {}", customerCode, tagDict);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeCustomerTag(String code, String tagDictKey) {
        customerMapper.ensureCustomerExist(code);
        customerTagMapper.deleteCustomerTag(code, tagDictKey);
    }

}
