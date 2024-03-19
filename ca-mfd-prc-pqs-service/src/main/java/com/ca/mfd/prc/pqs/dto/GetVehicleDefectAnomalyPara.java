package com.ca.mfd.prc.pqs.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eric.zhou
 * @date 2023/4/17
 */
@Data
public class GetVehicleDefectAnomalyPara {
    /**
     * 岗位
     */
    @JsonAlias(value = {"pmWorkplaceName", "PmWorkplaceName"})
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
     * 来源 -1全部 0 正常录入 1 百格图录入
     */
    private Integer source = -1;
    /**
     * 描述
     */
    private String description = StringUtils.EMPTY;
    /**
     * 责任区域
     */
    @JsonAlias(value = {"dutyarea", "DutyArea"})
    private String dutyArea;
    /**
     * QG岗名称
     */
    @JsonAlias(value = {"qgWorkplaceName", "QgWorkplaceName"})
    private String qgWorkplaceName = StringUtils.EMPTY;
    /**
     * 岗位集合
     */
    private List<String> workplaceIds = new ArrayList<>();
    /**
     * Code
     */
    @JsonAlias(value = {"tpsCode", "TpsCode"})
    private String tpsCode;
    private Integer pageIndex = 0;
    private Integer pageSize = 0;

    public GetVehicleDefectAnomalyPara() {
        this.status = -1;
        this.level = -1;
        this.source = -1;
        this.workplaceIds = new ArrayList<>();
    }
}
