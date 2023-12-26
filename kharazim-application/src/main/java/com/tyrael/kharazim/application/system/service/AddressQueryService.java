package com.tyrael.kharazim.application.system.service;


import com.tyrael.kharazim.application.system.dto.address.AddressDTO;
import com.tyrael.kharazim.application.system.dto.address.AddressTreeNodeDTO;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
public interface AddressQueryService {

    /**
     * list all
     *
     * @return addresses
     */
    List<AddressTreeNodeDTO> fullTree();

    /**
     * 根据节点ID查询tree
     *
     * @param nodeId 节点ID
     * @return address
     */
    AddressTreeNodeDTO addressTreeByNodeId(Long nodeId);

    /**
     * query by name
     *
     * @param name name
     * @return addresses
     */
    List<AddressDTO> queryByName(String name);

    /**
     * 所有省级地址
     *
     * @return 所有省级地址
     */
    List<AddressDTO> listProvince();

    /**
     * 查询省份下面的城市
     *
     * @param provinceCode 省份编码
     * @return 省份下面的城市
     */
    List<AddressDTO> queryCities(String provinceCode);

    /**
     * 查询城市下面的县（区）
     *
     * @param cityCode 城市编码
     * @return 城市下面的县（区）
     */
    List<AddressDTO> queryCounties(String cityCode);

}
