package com.ca.mfd.prc.pm.communication.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 阳波
 * @ClassName LjParamCurveDetailDTO
 * @description: 曲线参数明细
 * @date 2023年12月27日
 * @version: 1.0
 */
@Data
public class LjParamCurveDetailDTO {
    /**
     * 控制器名称(25 char)
     */
    private String controllerName = StringUtils.EMPTY;
    /**
     *VIN条码(25 char)
     */
    private String vin = StringUtils.EMPTY;
    /**
     * 程序号(0-999)
     */
    private Integer pSetId = 0;
    /**
     * 螺栓号(0-99)
     */
    private Integer boltNumber = 0;
    /**
     *拧紧ID(max 4294967295)
     */
    private Long tighteningID = 0L;
    /**
     * 采集时间间隔(0-0.99)
     */
    private Double timeCodfficient = 0D;
    /**
     * 曲线点数(0-99999)
     */
    private Integer pointCount = 0;
    /**
     * 扭力点集
     */
    private List<Double> torquePoints = new ArrayList<>();
    /**
     * 角度点集
     */
    private List<Double> anglePoints = new ArrayList<>();
    /**
     * 电流点集
     */
    private List<Double> currentPoints = new ArrayList<>();

}
