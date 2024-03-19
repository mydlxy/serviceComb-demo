package com.ca.mfd.prc.common.convert;


import com.ca.mfd.prc.common.enums.ConditionOper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * ConditionOper转换(解决get参数解析问题)
 *
 * @author inkelink eric.zhou
 */
@Component
public class ConditionOperConvert implements Converter<String, ConditionOper> {

    /**
     * convert
     *
     * @param source
     * @return ConditionOper
     */
    @Override
    public ConditionOper convert(String source) {
        if (StringUtils.isNotBlank(source)) {
            String txt = source.trim();
            if (StringUtils.isNumeric(txt)) {
                return Arrays.stream(ConditionOper.values()).filter(c -> c.value() == Integer.parseInt(txt)).findFirst().orElse(ConditionOper.Equal);
            } else {
                return Arrays.stream(ConditionOper.values()).filter(c -> StringUtils.equalsIgnoreCase(c.name(), txt)).findFirst().orElse(ConditionOper.Equal);
            }
        } else {
            return ConditionOper.Equal;
        }
    }
}
