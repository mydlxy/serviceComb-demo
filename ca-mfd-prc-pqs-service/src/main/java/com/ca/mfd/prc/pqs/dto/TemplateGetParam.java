package com.ca.mfd.prc.pqs.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: joel
 * @Date: 2023-08-18-11:02
 * @Description:
 */
@Data
public class TemplateGetParam {
    private String tempalteId = StringUtils.EMPTY;
}
