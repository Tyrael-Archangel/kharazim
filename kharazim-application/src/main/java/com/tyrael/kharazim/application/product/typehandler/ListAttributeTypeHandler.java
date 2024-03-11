package com.tyrael.kharazim.application.product.typehandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.tyrael.kharazim.application.product.vo.sku.Attribute;
import org.apache.commons.lang3.StringUtils;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/3/11
 */
public class ListAttributeTypeHandler extends AbstractJsonTypeHandler<List<Attribute>> {

    @Override
    public List<Attribute> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public List<Attribute> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public List<Attribute> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    @Override
    protected List<Attribute> parse(String json) {
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>();
        }
        return JSON.parseArray(json, Attribute.class);
    }

    @Override
    protected String toJson(List<Attribute> obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteNullListAsEmpty);
    }

}
