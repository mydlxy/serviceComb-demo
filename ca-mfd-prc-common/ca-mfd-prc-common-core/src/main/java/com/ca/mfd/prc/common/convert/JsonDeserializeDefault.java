/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.convert;

import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

/**
 * 通用反序列化(默认值 取属性初始值)
 *
 * @author inkelink eric.zhou@hg2mes.com
 * @date 2023-08-06
 */
public class JsonDeserializeDefault extends JsonDeserializer {

    private static final Logger logger = LoggerFactory.getLogger(JsonDeserializeDefault.class);

    /**
     * deserialize
     *
     * @param jsonParser jsonParser
     * @param deserializationContext deserializationContext
     * @return Object
     */
    @Override
    public Object deserialize(JsonParser jsonParser
            , DeserializationContext deserializationContext) throws IOException {
        String val = jsonParser.getText();
        String fieldName = jsonParser.getParsingContext().getCurrentName();
        Object resObj = jsonParser.getParsingContext().getCurrentValue();
        Class<?> defClass = resObj.getClass();
        Object defValue = getFieldValueByName(fieldName, resObj);
        if (StringUtils.isNotBlank(val)) {
            val = val.trim();
            Field fieldDeclare = getFieldAttrByName(fieldName, defClass);
            if (fieldDeclare == null) {
                return defValue == null ? val : defValue;
            }
            Class<?> fieldType = fieldDeclare.getType();
            if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                return Integer.valueOf(val.trim());
            } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                return ConvertUtils.stringToBoolean(val, defValue != null && ((Boolean) defValue));
            } else if (fieldType.equals(Date.class)) {
                val = DateUtils.replaceDefTime(val);
                return DateUtils.parse(val);
            } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                return ConvertUtils.stringToLong(val);
            } else if (fieldType.equals(String.class)) {
                return val;
            } else if (fieldType.equals(BigDecimal.class)) {
                return jsonParser.getDecimalValue();
            } else if (fieldType.equals(Double.class)) {
                return jsonParser.getDoubleValue();
            }
            //return jsonParser.getCurrentValue();
        }
        return defValue;
    }

    private Field getFieldAttrByName(String fieldName, Class<?> defClass) {
        Field[] selfFields = defClass.getDeclaredFields();
        if (selfFields.length > 0 && Arrays.stream(selfFields).anyMatch(c -> c.getName().equalsIgnoreCase(fieldName))) {
            return Arrays.stream(selfFields).filter(c -> c.getName().equalsIgnoreCase(fieldName)).findFirst().orElse(null);
        }
        Field[] superFields = defClass.getSuperclass().getDeclaredFields();
        if (superFields.length > 0 && Arrays.stream(superFields).anyMatch(c -> c.getName().equalsIgnoreCase(fieldName))) {
            return Arrays.stream(superFields).filter(c -> c.getName().equalsIgnoreCase(fieldName)).findFirst().orElse(null);
        }
        //都找不到，从JsonAlias中找
        for (Field ren : selfFields) {
            Annotation[] jsonalia = ren.getDeclaredAnnotationsByType(JsonAlias.class);
            if (jsonalia != null && jsonalia.length > 0) {
                JsonAlias jsonAlia = (JsonAlias) jsonalia[0];
                String[] jsor = jsonAlia.value();
                if (jsor != null && Arrays.stream(jsor).anyMatch(f -> StringUtils.equalsIgnoreCase(fieldName, f))) {
                    return ren;
                }
            }
        }
        //logger.error("{} 获取属性失败！", fieldName);
        return null;
    }

    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter);
            return method.invoke(o);
        } catch (Exception e) {
            //logger.error("获取属性值失败！" + e, e);
        }
        return null;
    }

}
