package com.tyrael.kharazim.application.supplier.service.impl;

import com.tyrael.kharazim.application.supplier.domain.SupplierDO;
import com.tyrael.kharazim.application.supplier.mapper.SupplierMapper;
import com.tyrael.kharazim.application.supplier.service.SupplierService;
import com.tyrael.kharazim.application.supplier.vo.PageSupplierRequest;
import com.tyrael.kharazim.application.supplier.vo.SupplierVO;
import com.tyrael.kharazim.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/2/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierMapper supplierMapper;

    @Override
    public PageResponse<SupplierVO> page(PageSupplierRequest pageRequest) {
        PageResponse<SupplierDO> pageData = supplierMapper.page(pageRequest);
        List<SupplierVO> units = pageData.getData()
                .stream()
                .map(this::supplierVO)
                .collect(Collectors.toList());
        return PageResponse.success(units,
                pageData.getTotalCount(),
                pageData.getPageSize(),
                pageData.getPageNum());
    }

    private SupplierVO supplierVO(SupplierDO supplier) {
        return SupplierVO.builder()
                .code(supplier.getCode())
                .name(supplier.getName())
                .remark(supplier.getRemark())
                .build();
    }

}
