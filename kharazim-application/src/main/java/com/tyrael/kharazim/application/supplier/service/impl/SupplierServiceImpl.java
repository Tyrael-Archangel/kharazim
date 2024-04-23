package com.tyrael.kharazim.application.supplier.service.impl;

import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.supplier.domain.SupplierDO;
import com.tyrael.kharazim.application.supplier.mapper.SupplierMapper;
import com.tyrael.kharazim.application.supplier.service.SupplierService;
import com.tyrael.kharazim.application.supplier.vo.AddSupplierRequest;
import com.tyrael.kharazim.application.supplier.vo.ListSupplierRequest;
import com.tyrael.kharazim.application.supplier.vo.PageSupplierRequest;
import com.tyrael.kharazim.application.supplier.vo.SupplierVO;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CodeGenerator codeGenerator;

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

    @Override
    public List<SupplierVO> list(ListSupplierRequest listRequest) {
        List<SupplierDO> suppliers = supplierMapper.list(listRequest);
        return suppliers.stream()
                .map(this::supplierVO)
                .collect(Collectors.toList());
    }

    private SupplierVO supplierVO(SupplierDO supplier) {
        return SupplierVO.builder()
                .code(supplier.getCode())
                .name(supplier.getName())
                .remark(supplier.getRemark())
                .creator(supplier.getCreator())
                .creatorCode(supplier.getCreatorCode())
                .createTime(supplier.getCreateTime())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(AddSupplierRequest addSupplierRequest) {
        SupplierDO supplier = new SupplierDO();
        supplier.setCode(codeGenerator.next(BusinessCodeConstants.SUPPLIER));
        supplier.setName(addSupplierRequest.getName());
        supplier.setRemark(addSupplierRequest.getRemark());

        try {
            supplierMapper.insert(supplier);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("供应商已存在", e);
        }

        return supplier.getCode();
    }

}
