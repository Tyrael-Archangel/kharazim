package com.tyrael.kharazim.application.customer.service;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.customer.vo.*;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
public interface CustomerService {

    /**
     * 查询会员基本信息
     *
     * @param code 会员编码
     * @return 会员基本信息
     */
    CustomerBaseVO findByCode(String code);

    /**
     * 新增会员
     *
     * @param addCustomerRequest AddCustomerRequest
     * @param currentUser        操作人
     * @return 新增的会员编码
     */
    String add(AddCustomerRequest addCustomerRequest, AuthUser currentUser);

    /**
     * 修改会员信息
     *
     * @param code                  会员编码
     * @param modifyCustomerRequest {@link ModifyCustomerRequest}
     * @param currentUser           操作人
     */
    void modify(String code, ModifyCustomerRequest modifyCustomerRequest, AuthUser currentUser);

    /**
     * 修改会员的来源会员
     *
     * @param modifySourceRequest {@link ModifyCustomerSourceRequest}
     * @param currentUser         操作人
     */
    void modifySource(ModifyCustomerSourceRequest modifySourceRequest, AuthUser currentUser);

    /**
     * 设置会员专属客服
     *
     * @param customerCode    会员编码
     * @param serviceUserCode 客服人员编码
     * @param currentUser     操作人
     */
    void assignCustomerServiceUser(String customerCode, String serviceUserCode, AuthUser currentUser);

    /**
     * 设置会员的专属销售顾问
     *
     * @param customerCode        会员编码
     * @param salesConsultantCode 销售顾问编码
     * @param currentUser         操作人
     */
    void assignCustomerSalesConsultant(String customerCode, String salesConsultantCode, AuthUser currentUser);

    /**
     * 新建会员地址
     *
     * @param addCustomerAddressRequest AddCustomerAddressRequest
     * @return 会员地址ID
     */
    Long addAddress(AddCustomerAddressRequest addCustomerAddressRequest);

    /**
     * 新增会员保险
     *
     * @param addCustomerInsuranceRequest AddCustomerInsuranceRequest
     * @return 会员保险ID
     */
    Long addInsurance(AddCustomerInsuranceRequest addCustomerInsuranceRequest);

}
