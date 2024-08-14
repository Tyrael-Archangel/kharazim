package com.tyrael.kharazim.application.pharmacy.service;

import com.tyrael.kharazim.application.pharmacy.vo.inventory.InventoryVO;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.ListInventoryOfClinicRequest;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.PageInventoryRequest;
import com.tyrael.kharazim.common.dto.PageResponse;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
public interface InventoryService {

    /**
     * 库存分页
     *
     * @param pageRequest {@link PageInventoryRequest}
     * @return 库存分页数据
     */
    PageResponse<InventoryVO> page(PageInventoryRequest pageRequest);

    /**
     * 查询诊所库存数据
     *
     * @param listRequest 诊所+sku
     * @return 诊所库存数据
     */
    List<InventoryVO> listOfClinic(ListInventoryOfClinicRequest listRequest);

}
