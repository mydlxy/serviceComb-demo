/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.convert;

import com.ca.mfd.prc.common.utils.DateUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Date;

/**
 * Date反序列化(默认值为 new Date())
 *
 * @author inkelink eric.zhou
 * @date 2023-08-21
 */
public class JsonDeserializeDateDefNow extends JsonDeserializer {

    /**
     * deserialize
     *
     * @param jsonParser
     * @param deserializationContext
     * @return Object
     */
    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String val = jsonParser.getText();
        if (StringUtils.isNotBlank(val) && val.trim().length() > 0) {
            val = DateUtils.replaceDefTime(val);
            if (val.matches("^\\d{4}-\\d{1,2}$")) {
                return DateUtils.parse(val, DateUtils.FORMAT_LIST.get(0));
            } else if (val.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
                return DateUtils.parse(val, DateUtils.FORMAT_LIST.get(1));
            } else if (val.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
                return DateUtils.parse(val, DateUtils.FORMAT_LIST.get(2));
            } else if (val.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                return DateUtils.parse(val, DateUtils.FORMAT_LIST.get(3));
            } else if (val.matches("^\\d{4}-\\d{1,2}-\\d{1,2}.*T.*\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                return DateUtils.parse(val, DateUtils.FORMAT_LIST.get(4));
            } else if (val.matches("^\\d{4}-\\d{1,2}-\\d{1,2}.*T.*\\d{1,2}:\\d{1,2}:\\d{1,2}.*..*$")) {
                return DateUtils.parse(val, DateUtils.FORMAT_LIST.get(5));
            } else {
                throw new IllegalArgumentException("Invalid date value '" + val + "'");
            }
        } else {
            return new Date();
        }
    }

}
