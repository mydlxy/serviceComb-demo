package com.ca.mfd.prc.eps.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * UpdateVehicleDefectAnomalyPara
 *
 * @author inkelink
 * @date 2023/09/05
 */
@Data
public class UpdateVehicleDefectAnomalyPara {
    private String dataId = StringUtils.EMPTY;

    private Integer level = 0;

    /**
     * 责任区域
     */
    private String dutyArea = StringUtils.EMPTY;

    /**
     * 缺陷代码 组件-分类-代码
     */
    private String code = StringUtils.EMPTY;

    /**
     * 缺陷描述 组件-分类-描述
     */
    private String description = StringUtils.EMPTY;
}
