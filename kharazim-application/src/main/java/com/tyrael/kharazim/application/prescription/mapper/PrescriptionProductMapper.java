package com.tyrael.kharazim.application.prescription.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.tyrael.kharazim.application.prescription.domain.PrescriptionProduct;
import org.apache.ibatis.annotations.Mapper;

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

}
