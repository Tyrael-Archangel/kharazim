package com.tyrael.kharazim.diagnosistreatment.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.tyrael.kharazim.diagnosistreatment.app.domain.prescription.PrescriptionProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
@Mapper
public interface PrescriptionProductMapper extends BaseMapper<PrescriptionProduct> {

    /**
     * 批量保存
     *
     * @param products 处方商品明细
     */
    default void saveBatch(List<PrescriptionProduct> products) {
        Db.saveBatch(products);
    }

    /**
     * list by  prescriptionCode
     *
     * @param prescriptionCode 处方编码
     * @return PrescriptionProducts
     */
    default List<PrescriptionProduct> listByPrescriptionCode(String prescriptionCode) {
        return listByPrescriptionCodes(Collections.singleton(prescriptionCode));
    }

    /**
     * list by prescriptionCodes
     *
     * @param prescriptionCodes 处方编码
     * @return PrescriptionProducts
     */
    default List<PrescriptionProduct> listByPrescriptionCodes(Collection<String> prescriptionCodes) {
        if (prescriptionCodes == null || prescriptionCodes.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<PrescriptionProduct> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(PrescriptionProduct::getPrescriptionCode, prescriptionCodes);
        return selectList(queryWrapper);
    }

}
