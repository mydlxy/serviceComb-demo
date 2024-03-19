package com.ca.mfd.prc.eps.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * UpdateVehicleDefectAnomalyLevelPara
 *
 * @author inkelink
 * @date 2023/09/05
 */
@Data
public class UpdateVehicleDefectAnomalyLevelPara {

    private String dataId = StringUtils.EMPTY;

    private Integer level = 0;

    /**
     * 责任区域
     */
    @JsonAlias(value = {"DutyArea", "dutyArea"})
    private String dutyArea = StringUtils.EMPTY;
}
