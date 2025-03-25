package com.tyrael.kharazim.purchase.app.service.impl;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
import com.tyrael.kharazim.purchase.app.constant.PurchaseBusinessIdConstants;
import com.tyrael.kharazim.purchase.app.domain.SupplierDO;
import com.tyrael.kharazim.purchase.app.mapper.SupplierMapper;
import com.tyrael.kharazim.purchase.app.service.SupplierService;
import com.tyrael.kharazim.purchase.app.vo.supplier.AddSupplierRequest;
import com.tyrael.kharazim.purchase.app.vo.supplier.ListSupplierRequest;
import com.tyrael.kharazim.purchase.app.vo.supplier.PageSupplierRequest;
import com.tyrael.kharazim.purchase.app.vo.supplier.SupplierDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
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
    private final IdGenerator idGenerator;

    @Override
    public List<SupplierDTO> listByCodes(Collection<String> codes) {
        List<SupplierDO> suppliers = supplierMapper.listByCodes(codes);
        return suppliers.stream()
                .map(this::supplierVO)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierDTO findByCode(String code) {
        SupplierDO supplierDO = supplierMapper.findByCode(code);
        return this.supplierVO(supplierDO);
    }

    @Override
    public PageResponse<SupplierDTO> page(PageSupplierRequest pageRequest) {
        PageResponse<SupplierDO> pageData = supplierMapper.page(pageRequest);
        List<SupplierDTO> units = pageData.getData()
                .stream()
                .map(this::supplierVO)
                .collect(Collectors.toList());
        return PageResponse.success(units, pageData.getTotalCount());
    }

    @Override
    public List<SupplierDTO> list(ListSupplierRequest listRequest) {
        List<SupplierDO> suppliers = supplierMapper.list(listRequest);
        return suppliers.stream()
                .map(this::supplierVO)
                .collect(Collectors.toList());
    }

    private SupplierDTO supplierVO(SupplierDO supplier) {
        return SupplierDTO.builder()
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
        supplier.setCode(idGenerator.next(PurchaseBusinessIdConstants.SUPPLIER));
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
