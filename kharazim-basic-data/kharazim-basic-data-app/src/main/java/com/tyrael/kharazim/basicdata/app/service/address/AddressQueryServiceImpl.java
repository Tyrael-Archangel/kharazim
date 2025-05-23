package com.tyrael.kharazim.basicdata.app.service.address;

import com.tyrael.kharazim.base.dto.TreeNode;
import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.app.constant.CacheKeyConstants;
import com.tyrael.kharazim.basicdata.app.domain.address.Address;
import com.tyrael.kharazim.basicdata.app.dto.address.AddressDTO;
import com.tyrael.kharazim.basicdata.app.dto.address.AddressTreeNodeDTO;
import com.tyrael.kharazim.basicdata.app.mapper.address.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tyrael.kharazim.basicdata.app.enums.AddressLevelEnum.*;


/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@Service
@RequiredArgsConstructor
public class AddressQueryServiceImpl implements AddressQueryService {

    private final AddressMapper addressMapper;

    @Override
    @Cacheable(CacheKeyConstants.ADDRESS_TREE)
    public List<AddressTreeNodeDTO> fullTree() {
        List<Address> addresses = addressMapper.listAll();
        return TreeNode.build(addressTreeNodes(addresses));
    }

    @Override
    @Cacheable(value = CacheKeyConstants.ADDRESS_TREE_NODE, key = "#nodeId")
    public AddressTreeNodeDTO addressTreeByNodeId(Long nodeId) {
        Address root = addressMapper.selectById(nodeId);
        DomainNotFoundException.assertFound(root, "Address node id: " + nodeId);
        List<Address> addresses = addressMapper.listByNodeId(nodeId);
        addresses.add(root);
        return TreeNode.build(addressTreeNodes(addresses)).get(0);
    }

    private List<AddressTreeNodeDTO> addressTreeNodes(List<Address> addresses) {
        if (CollectionUtils.isEmpty(addresses)) {
            return new ArrayList<>();
        }
        return addresses.stream()
                .map(address -> {
                    AddressTreeNodeDTO addressTreeNodeDTO = new AddressTreeNodeDTO();
                    addressTreeNodeDTO.setId(address.getId());
                    addressTreeNodeDTO.setCode(address.getCode());
                    addressTreeNodeDTO.setName(address.getName());
                    addressTreeNodeDTO.setParentId(address.getParentId());
                    addressTreeNodeDTO.setLevel(address.getLevel());
                    return addressTreeNodeDTO;
                })
                .toList();
    }

    @Override
    public List<AddressDTO> queryByName(String name) {
        List<Address> addresses = addressMapper.queryByName(name);
        return this.addressDtoList(addresses);
    }

    @Override
    @Cacheable(CacheKeyConstants.ADDRESS_PROVINCE)
    public List<AddressDTO> listProvince() {
        List<Address> addresses = addressMapper.queryByLevelAndParentId(PROVINCE, null);
        return this.addressDtoList(addresses);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.ADDRESS_LIST, key = "#provinceCode")
    public List<AddressDTO> queryCities(String provinceCode) {
        Address province = this.queryByCodeExactly(provinceCode);
        if (!PROVINCE.equals(province.getLevel())) {
            throw new BusinessException("省份有误");
        }
        List<Address> addresses = addressMapper.queryByLevelAndParentId(CITY, province.getId());
        return this.addressDtoList(addresses);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.ADDRESS_LIST, key = "#cityCode")
    public List<AddressDTO> queryCounties(String cityCode) {
        Address city = this.queryByCodeExactly(cityCode);
        if (!CITY.equals(city.getLevel())) {
            throw new BusinessException("城市有误");
        }
        List<Address> addresses = addressMapper.queryByLevelAndParentId(COUNTY, city.getId());
        return this.addressDtoList(addresses);
    }

    private Address queryByCodeExactly(String code) {
        Address address = addressMapper.queryByCode(code);
        DomainNotFoundException.assertFound(address, code);
        return address;
    }

    private List<AddressDTO> addressDtoList(List<Address> addresses) {
        if (CollectionUtils.isEmpty(addresses)) {
            return new ArrayList<>();
        }
        return addresses.stream()
                .map(address -> {
                    AddressDTO addressDTO = new AddressDTO();
                    addressDTO.setId(address.getId());
                    addressDTO.setCode(address.getCode());
                    addressDTO.setName(address.getName());
                    addressDTO.setParentId(address.getParentId());
                    addressDTO.setLevel(address.getLevel());
                    return addressDTO;
                })
                .collect(Collectors.toList());
    }

}
