package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author eric.zhou
 * @date 2023/4/17
 */
@Data
public class VehicleDefectAnomalyInfo {

    /**
     * 数据编号
     */
    private String dataId = StringUtils.EMPTY;

    /**
     * 激活缺陷工位
     */
    private String workplaceName;


    /**
     * 缺陷编号
     */
    private String anomalyId = StringUtils.EMPTY;

    /**
     * 缺陷代码
     */
    private String anomalyCode;

    /**
     * 缺陷描述
     */
    private String anomalyDescription;

    /**
     * 图片
     */
    private String img;

    /**
     * 等级
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer level = 0;

    /**
     * 等级描述
     */
    private String levelDis;

    /**
     * 责任区域
     */
    private String pqsDutyArea;


    /**
     * 状态描述
     */
    private String statusDis;

    /**
     * (1.未修复;2.已修复;3.未发现;4.合格;5.不合格)
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;

    /**
     * 操作编号
     */
    private String woId = StringUtils.EMPTY;

    /**
     * 是否拧紧工艺
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isScrew = false;

    /**
     * JsonData
     */
    private String jsonData = StringUtils.EMPTY;

    /**
     * 来源 0 正常录入 1百格图录入
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer source = 0;
}
