package com.tyrael.kharazim.basicdata.app.service.customer;

import com.tyrael.kharazim.authentication.Principal;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.basicdata.app.dto.customer.customer.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

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
     * 会员分页
     *
     * @param pageRequest {@link PageCustomerRequest}
     * @return 会员分页数据
     */
    PageResponse<CustomerBaseVO> page(PageCustomerRequest pageRequest);

    /**
     * 导出会员数据
     *
     * @param pageRequest         {@link PageCustomerRequest}
     * @param httpServletResponse HttpServletResponse
     * @throws IOException IOException
     */
    void export(PageCustomerRequest pageRequest, HttpServletResponse httpServletResponse) throws IOException;

    /**
     * 新增会员
     *
     * @param addCustomerRequest AddCustomerRequest
     * @param currentUser        操作人
     * @return 新增的会员编码
     */
    String add(AddCustomerRequest addCustomerRequest, Principal currentUser);

    /**
     * 修改会员信息
     *
     * @param code                  会员编码
     * @param modifyCustomerRequest {@link ModifyCustomerRequest}
     * @param currentUser           操作人
     */
    void modify(String code, ModifyCustomerRequest modifyCustomerRequest, Principal currentUser);

    /**
     * 修改会员的来源会员
     *
     * @param modifySourceRequest {@link ModifyCustomerSourceRequest}
     * @param currentUser         操作人
     */
    void modifySource(ModifyCustomerSourceRequest modifySourceRequest, Principal currentUser);

    /**
     * 绑定手机号
     *
     * @param bindCustomerPhoneRequest {@link BindCustomerPhoneRequest}
     * @param currentUser              操作人
     */
    void bindPhone(BindCustomerPhoneRequest bindCustomerPhoneRequest, Principal currentUser);

    /**
     * 解绑手机号
     *
     * @param code        会员编码
     * @param currentUser 操作人
     */
    void unbindPhone(String code, Principal currentUser);

    /**
     * 根据指定条件（姓名、电话、证件号码）过滤用户
     *
     * @param request ListCustomerRequestVO
     * @return 用户基本信息
     */
    List<CustomerSimpleVO> listSimpleInfo(ListCustomerRequest request);

    /**
     * 查询会员地址
     *
     * @param code 会员编码
     * @return 会员地址
     */
    List<CustomerAddressVO> addresses(String code);

    /**
     * 新建会员地址
     *
     * @param addCustomerAddressRequest AddCustomerAddressRequest
     * @return 会员地址ID
     */
    Long addAddress(AddCustomerAddressRequest addCustomerAddressRequest);

    /**
     * 删除会员地址
     *
     * @param customerCode      会员编码
     * @param customerAddressId 会员地址ID
     */
    void deleteCustomerAddress(String customerCode, Long customerAddressId);

    /**
     * 修改会员地址
     *
     * @param modifyCustomerAddressRequest {@link ModifyCustomerAddressRequest}
     * @param currentUser                  操作人
     */
    void modifyCustomerAddress(ModifyCustomerAddressRequest modifyCustomerAddressRequest, Principal currentUser);

    /**
     * 将指定的会员地址设置为默认地址
     *
     * @param customerCode      会员编码
     * @param customerAddressId 会员地址ID
     */
    void markAddressDefault(String customerCode, Long customerAddressId);

    /**
     * 查询会员保险
     *
     * @param code 会员编码
     * @return 会员保险
     */
    List<CustomerInsuranceVO> insurances(String code);

    /**
     * 新增会员保险
     *
     * @param addCustomerInsuranceRequest AddCustomerInsuranceRequest
     * @return 会员保险ID
     */
    Long addInsurance(AddCustomerInsuranceRequest addCustomerInsuranceRequest);

    /**
     * 删除会员保险
     *
     * @param customerCode        会员编码
     * @param customerInsuranceId 会员保险ID
     */
    void deleteCustomerInsurance(String customerCode, Long customerInsuranceId);

    /**
     * 修改会员保险
     *
     * @param modifyCustomerInsuranceRequest {@link ModifyCustomerInsuranceRequest}
     * @param currentUser                    操作人
     */
    void modifyCustomerInsurance(ModifyCustomerInsuranceRequest modifyCustomerInsuranceRequest, Principal currentUser);

    /**
     * 将指定的会员保险设置为默认保险
     *
     * @param customerCode        会员编码
     * @param customerInsuranceId 会员保险ID
     */
    void markInsuranceDefault(String customerCode, Long customerInsuranceId);

    /**
     * 查询会员的专属客服
     *
     * @param customerCode 会员编码
     * @return 会员的专属客服
     */
    CustomerServiceUserVO customerService(String customerCode);

    /**
     * 设置会员专属客服
     *
     * @param customerCode    会员编码
     * @param serviceUserCode 客服人员编码
     * @param currentUser     操作人
     */
    void assignCustomerServiceUser(String customerCode, String serviceUserCode, Principal currentUser);

    /**
     * 查询会员的专属销售顾问
     *
     * @param code 会员编码
     * @return 会员的专属销售顾问
     */
    CustomerSalesConsultantVO customerSalesConsultant(String code);

    /**
     * 设置会员的专属销售顾问
     *
     * @param customerCode        会员编码
     * @param salesConsultantCode 销售顾问编码
     * @param currentUser         操作人
     */
    void assignCustomerSalesConsultant(String customerCode, String salesConsultantCode, Principal currentUser);

    /**
     * 查询会员的标签
     *
     * @param code 会员编码
     * @return 会员的标签
     */
    List<CustomerTagVO> customerTags(String code);

    /**
     * 为会员添加标签
     *
     * @param addCustomerTagRequest AddCustomerTagRequest
     * @param currentUser           操作人
     */
    void addCustomerTag(AddCustomerTagRequest addCustomerTagRequest, Principal currentUser);

    /**
     * 删除会员的标签
     *
     * @param code       会员编码
     * @param tagDictKey 会员标签字典键
     */
    void removeCustomerTag(String code, String tagDictKey);

}
