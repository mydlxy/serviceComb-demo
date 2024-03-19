/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.convert;

import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Boolean反序列化(默认值false)
 *
 * @author inkelink eric.zhou@hg2mes.com
 * @date 2023-08-06
 */
public class JsonDeserializeBoolean extends JsonDeserializer {

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
        return ConvertUtils.stringToBoolean(val, false);
    }

}
