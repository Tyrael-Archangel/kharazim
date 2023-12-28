package com.tyrael.kharazim.application.system.typehandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.google.common.collect.Lists;
import com.tyrael.kharazim.application.system.dto.requestlog.NameAndValue;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
public class ListNameValueTypeHandler extends AbstractJsonTypeHandler<List<NameAndValue>> {

    @Override
    public List<NameAndValue> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public List<NameAndValue> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public List<NameAndValue> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    @Override
    protected List<NameAndValue> parse(String json) {
        if (!StringUtils.hasText(json)) {
            return Lists.newArrayList();
        }
        return JSON.parseArray(json, NameAndValue.class);
    }

    @Override
    protected String toJson(List<NameAndValue> obj) {
        List<NameAndValue> list = obj == null ? Lists.newArrayList() : obj;
        return JSON.toJSONString(list,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty);
    }

}
