package com.ca.mfd.prc.avi.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 车辆识别码扫描过点
 *
 * @author banny.luo
 * @since 1.0.0 2023-04-06
 */
@Data
public class TpsCodeSanDTO {
    /**
     * 关键点标识
     */
    private String aviId = StringUtils.EMPTY;
    /**
     * 车辆识别码
     */
    private String tpsCode = StringUtils.EMPTY;
}
