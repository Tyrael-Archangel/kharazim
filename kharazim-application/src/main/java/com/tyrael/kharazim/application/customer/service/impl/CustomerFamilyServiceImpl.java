package com.tyrael.kharazim.application.customer.service.impl;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.customer.domain.Customer;
import com.tyrael.kharazim.application.customer.domain.Family;
import com.tyrael.kharazim.application.customer.domain.FamilyMember;
import com.tyrael.kharazim.application.customer.mapper.CustomerMapper;
import com.tyrael.kharazim.application.customer.mapper.FamilyMapper;
import com.tyrael.kharazim.application.customer.mapper.FamilyMemberMapper;
import com.tyrael.kharazim.application.customer.service.CustomerFamilyService;
import com.tyrael.kharazim.application.customer.vo.family.*;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import com.tyrael.kharazim.common.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/1/19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerFamilyServiceImpl implements CustomerFamilyService {

    private static final String LEADER_NAME = "户主";

    private final FamilyMapper familyMapper;
    private final FamilyMemberMapper familyMemberMapper;
    private final CustomerMapper customerMapper;
    private final CodeGenerator codeGenerator;

    @Override
    public CustomerFamilyVO family(String familyCode) {
        Family family = familyMapper.findByCode(familyCode);
        DomainNotFoundException.assertFound(family, familyCode);

        List<FamilyMember> familyMembers = familyMemberMapper.listByFamilyCode(familyCode);

        Set<String> customerCodes = familyMembers.stream()
                .map(FamilyMember::getCustomerCode)
                .collect(Collectors.toSet());
        customerCodes.add(family.getLeaderCode());
        Map<String, Customer> customerMap = customerMapper.mapByCodes(customerCodes);

        return customerFamilyVO(family, familyMembers, customerMap);
    }

    private CustomerFamilyVO customerFamilyVO(Family family,
                                              List<FamilyMember> familyMembers,
                                              Map<String, Customer> customerMap) {

        Customer leaderCustomer = customerMap.get(family.getLeaderCode());
        List<CustomerFamilyVO.FamilyMemberVO> familyMemberVOList = new ArrayList<>();
        CustomerFamilyVO.FamilyMemberVO leader = new CustomerFamilyVO.FamilyMemberVO();
        leader.setName(leaderCustomer.getName());
        leader.setCustomerCode(leaderCustomer.getCode());
        leader.setPhone(leaderCustomer.getPhone());
        leader.setRelationToLeader(LEADER_NAME);
        familyMemberVOList.add(leader);

        for (FamilyMember familyMember : familyMembers) {
            if (!StringUtils.equals(familyMember.getCustomerCode(), leaderCustomer.getCode())) {
                Customer familyMemberCustomer = customerMap.get(familyMember.getCustomerCode());

                CustomerFamilyVO.FamilyMemberVO familyMemberVO = new CustomerFamilyVO.FamilyMemberVO();
                familyMemberVO.setName(familyMemberCustomer.getName());
                familyMemberVO.setCustomerCode(familyMemberCustomer.getCode());
                familyMemberVO.setPhone(familyMemberCustomer.getPhone());
                familyMemberVO.setRelationToLeader(familyMember.getRelationToLeader());
                familyMemberVOList.add(familyMemberVO);
            }
        }

        CustomerFamilyVO customerFamily = new CustomerFamilyVO();
        customerFamily.setFamilyCode(family.getCode());
        customerFamily.setFamilyName(family.getName());
        customerFamily.setLeaderCustomerCode(leaderCustomer.getCode());
        customerFamily.setLeaderCustomerName(leaderCustomer.getName());
        customerFamily.setRemark(family.getRemark());
        customerFamily.setFamilyMembers(familyMemberVOList);

        return customerFamily;
    }

    @Override
    public PageResponse<CustomerFamilyVO> page(PageFamilyRequest pageRequest) {
        PageResponse<Family> familyPage = familyMapper.page(pageRequest);
        return PageResponse.success(this.customerFamilyVO(familyPage.getData()), familyPage.getTotalCount());
    }

    private List<CustomerFamilyVO> customerFamilyVO(Collection<Family> families) {
        if (CollectionUtils.isEmpty(families)) {
            return new ArrayList<>();
        }
        Set<String> familyCodes = families.stream()
                .map(Family::getCode)
                .collect(Collectors.toSet());

        List<FamilyMember> allFamilyMembers = familyMemberMapper.listByFamilyCodes(familyCodes);
        Map<String, List<FamilyMember>> familyMemberMap = allFamilyMembers.stream()
                .collect(Collectors.groupingBy(FamilyMember::getFamilyCode));
        Set<String> customerCodes = allFamilyMembers.stream()
                .map(FamilyMember::getCustomerCode)
                .collect(Collectors.toSet());
        Map<String, Customer> customerMap = customerMapper.mapByCodes(customerCodes);

        return families.stream()
                .map(family -> customerFamilyVO(family, familyMemberMap.get(family.getCode()), customerMap))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(CreateFamilyRequest createFamilyRequest) {

        String leaderCode = createFamilyRequest.getLeaderCustomerCode();
        customerMapper.ensureCustomerExist(leaderCode);

        String familyCode = codeGenerator.next(BusinessCodeConstants.CUSTOMER_FAMILY);

        Family family = new Family();
        family.setCode(familyCode);
        family.setName(createFamilyRequest.getFamilyName());
        family.setLeaderCode(leaderCode);
        family.setRemark(createFamilyRequest.getRemark());

        familyMapper.insert(family);

        addFamilyMember(new AddFamilyMemberRequest(familyCode, leaderCode, LEADER_NAME));

        return familyCode;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFamilyMember(AddFamilyMemberRequest addFamilyMemberRequest) {
        String familyCode = addFamilyMemberRequest.getFamilyCode();
        String customerCode = addFamilyMemberRequest.getCustomerCode();
        customerMapper.ensureCustomerExist(customerCode);
        Family family = familyMapper.findByCode(familyCode);
        DomainNotFoundException.assertFound(family, familyCode);

        FamilyMember familyMember = new FamilyMember();
        familyMember.setFamilyCode(familyCode);
        familyMember.setCustomerCode(customerCode);
        familyMember.setRelationToLeader(addFamilyMemberRequest.getRelationToLeader());

        try {
            familyMemberMapper.insert(familyMember);
        } catch (DuplicateKeyException e) {
            log.error("save family member error: {}", e.getMessage());
            throw new BusinessException("会员已经属于该家庭，请勿重复加入");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setLeader(String customerCode, String familyCode, AuthUser currentUser) {
        Family family = familyMapper.findByCode(familyCode);
        DomainNotFoundException.assertFound(family, familyCode);

        FamilyMember familyMember = familyMemberMapper.findByCustomerCode(familyCode, customerCode);
        DomainNotFoundException.assertFound(familyMember, customerCode);

        family.setLeaderCode(customerCode);
        family.setUpdateUser(currentUser.getCode(), currentUser.getNickName());
        familyMapper.updateById(family);

        familyMember.setRelationToLeader(LEADER_NAME);
        familyMember.setUpdateUser(currentUser.getCode(), currentUser.getNickName());
        familyMemberMapper.updateById(familyMember);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyFamilyMemberRelation(ModifyFamilyMemberRelationRequest modifyRelationRequest,
                                           AuthUser currentUser) {
        String familyCode = modifyRelationRequest.getFamilyCode();
        Family family = familyMapper.findByCode(familyCode);
        DomainNotFoundException.assertFound(family, familyCode);

        String customerCode = modifyRelationRequest.getCustomerCode();
        FamilyMember familyMember = familyMemberMapper.findByCustomerCode(familyCode, customerCode);
        DomainNotFoundException.assertFound(familyMember, customerCode);

        familyMember.setRelationToLeader(modifyRelationRequest.getRelationToLeader());
        familyMember.setUpdateUser(currentUser.getCode(), currentUser.getNickName());
        familyMemberMapper.updateById(familyMember);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void leaveFamily(LeaveFamilyRequest leaveFamilyRequest) {
        String familyCode = leaveFamilyRequest.getFamilyCode();
        String customerCode = leaveFamilyRequest.getCustomerCode();
        customerMapper.ensureCustomerExist(customerCode);

        customerLeaveFamily(familyCode, customerCode);
    }

    private void customerLeaveFamily(String familyCode, String customerCode) {
        Family family = familyMapper.findByCode(familyCode);
        DomainNotFoundException.assertFound(family, familyCode);

        if (StringUtils.equals(family.getLeaderCode(), customerCode)) {
            // 户主要离开家庭，必须满足家庭中只剩户主才能离开家庭，同时解散该家庭
            List<FamilyMember> familyMembers = familyMemberMapper.listByFamilyCode(familyCode);
            boolean familyContainsOtherMember = familyMembers.stream()
                    .anyMatch(e -> !StringUtils.equals(e.getCustomerCode(), customerCode));
            if (familyContainsOtherMember) {
                throw new BusinessException("家庭中存在其他成员，户主不能离开该家庭");
            }

            // familyMembers为空或者只包含户主
            for (FamilyMember familyMember : familyMembers) {
                familyMemberMapper.leave(familyCode, familyMember.getCustomerCode());
            }
            familyMapper.deleteById(family.getId());

        } else {
            // 非户主离开该家庭
            familyMemberMapper.leave(familyCode, customerCode);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerFamilyVO> customerFamily(String customerCode) {
        customerMapper.ensureCustomerExist(customerCode);

        List<FamilyMember> familyMembers = familyMemberMapper.listByCustomerCode(customerCode);
        Set<String> familyCodes = familyMembers.stream()
                .map(FamilyMember::getFamilyCode)
                .collect(Collectors.toSet());
        List<Family> families = familyMapper.listByCodes(familyCodes);

        return customerFamilyVO(families);
    }

}
