package com.tyrael.kharazim.application.customer.service.impl;

import com.google.common.collect.Lists;
import com.tyrael.kharazim.application.customer.domain.Customer;
import com.tyrael.kharazim.application.customer.domain.Family;
import com.tyrael.kharazim.application.customer.domain.FamilyMember;
import com.tyrael.kharazim.application.customer.mapper.CustomerMapper;
import com.tyrael.kharazim.application.customer.mapper.FamilyMapper;
import com.tyrael.kharazim.application.customer.mapper.FamilyMemberMapper;
import com.tyrael.kharazim.application.customer.service.CustomerFamilyService;
import com.tyrael.kharazim.application.customer.vo.family.CustomerFamilyVO;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
        List<CustomerFamilyVO.FamilyMemberVO> familyMemberVOList = Lists.newArrayList();
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
}
