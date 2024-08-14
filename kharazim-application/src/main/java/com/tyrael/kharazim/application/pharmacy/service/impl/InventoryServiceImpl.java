package com.tyrael.kharazim.application.pharmacy.service.impl;

import com.tyrael.kharazim.application.pharmacy.converter.InventoryConverter;
import com.tyrael.kharazim.application.pharmacy.domain.Inventory;
import com.tyrael.kharazim.application.pharmacy.mapper.InventoryMapper;
import com.tyrael.kharazim.application.pharmacy.service.InventoryService;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.InventoryVO;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.ListInventoryOfClinicRequest;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.PageInventoryRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryMapper inventoryMapper;
    private final InventoryConverter inventoryConverter;

    @Override
    public PageResponse<InventoryVO> page(PageInventoryRequest pageRequest) {
        PageResponse<Inventory> pageData = inventoryMapper.page(pageRequest);
        return PageResponse.success(
                inventoryConverter.inventories(pageData.getData()),
                pageData.getTotalCount(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }

    @Override
    public List<InventoryVO> listOfClinic(ListInventoryOfClinicRequest listRequest) {
        List<Inventory> inventories = inventoryMapper.listOfClinic(listRequest);
        return inventoryConverter.inventories(inventories);
    }

}
