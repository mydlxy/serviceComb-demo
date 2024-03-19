package com.ca.mfd.prc.eps.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * GetVehicleDefectAnomalyByLeaderPara
 *
 * @author inkelink
 * @date 2023/09/05
 */
@Data
public class GetVehicleDefectAnomalyByLeaderPara {

    /**
     * 岗位
     */
    @JsonAlias(value = {"PmWorkplaceName", "pmworkplacename"})
    private String pmWorkplaceName = StringUtils.EMPTY;

    /**
     * 状态
     */
    @JsonAlias(value = {"Status", "status"})
    private Integer status = -1;

    /**
     * 等级
     */
    @JsonAlias(value = {"Level", "level"})
    private Integer level = -1;

    /**
     * 描述
     */
    @JsonAlias(value = {"Description", "description"})
    private String description = StringUtils.EMPTY;

    /**
     * 责任区域
     */
    @JsonAlias(value = {"DutyArea", "dutyarea"})
    private String dutyArea = StringUtils.EMPTY;

    /**
     * 产品编号
     */
    @JsonAlias(value = {"ProductCode", "productcode"})
    private String productCode = StringUtils.EMPTY;

    private Integer pageIndex = 0;

    private Integer pageSize = 0;
}
