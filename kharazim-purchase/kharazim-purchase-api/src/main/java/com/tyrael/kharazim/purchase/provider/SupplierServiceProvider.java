package com.tyrael.kharazim.purchase.provider;

import com.tyrael.kharazim.purchase.app.service.SupplierService;
import com.tyrael.kharazim.purchase.app.vo.supplier.ListSupplierRequest;
import com.tyrael.kharazim.purchase.app.vo.supplier.SupplierDTO;
import com.tyrael.kharazim.purchase.sdk.model.SupplierVO;
import com.tyrael.kharazim.purchase.sdk.service.SupplierServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/3/25
 */
@Component
@DubboService
@RequiredArgsConstructor
public class SupplierServiceProvider implements SupplierServiceApi {

    private final SupplierService supplierService;

    @Override
    public SupplierVO findByCode(String code) {
        SupplierDTO supplierDTO = supplierService.findByCode(code);
        return supplierVO(supplierDTO);
    }

    @Override
    public Map<String, SupplierVO> mapByCodes(Collection<String> codes) {
        List<SupplierDTO> suppliers = supplierService.listByCodes(codes);
        return suppliers.stream()
                .map(this::supplierVO)
                .collect(Collectors.toMap(SupplierVO::getCode, e -> e));
    }

    @Override
    public List<SupplierVO> listAll() {
        List<SupplierDTO> suppliers = supplierService.list(new ListSupplierRequest());
        return suppliers.stream()
                .map(this::supplierVO)
                .collect(Collectors.toList());
    }

    private SupplierVO supplierVO(SupplierDTO supplierDTO) {
        return SupplierVO.builder()
                .code(supplierDTO.getCode())
                .name(supplierDTO.getName())
                .remark(supplierDTO.getRemark())
                .creator(supplierDTO.getCreator())
                .creatorCode(supplierDTO.getCreatorCode())
                .createTime(supplierDTO.getCreateTime())
                .build();
    }

}
