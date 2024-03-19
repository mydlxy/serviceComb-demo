package com.ca.mfd.prc.avi.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 车辆识别码
 *
 * @author banny.luo
 * @since 1.0.0 2023-04-06
 */
@Data
public class TpsPrintParamDTO {
    private String tpsCode = StringUtils.EMPTY;

    private String aviCode = StringUtils.EMPTY;
}
