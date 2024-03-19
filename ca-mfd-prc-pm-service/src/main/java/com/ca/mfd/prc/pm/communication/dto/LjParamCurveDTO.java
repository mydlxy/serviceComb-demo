package com.ca.mfd.prc.pm.communication.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 阳波
 * @ClassName LjParamCurveDTO
 * @description: 曲线参数信息
 * @date 2023年12月27日
 * @version: 1.0
 */
@Data
public class LjParamCurveDTO {
    /**
     *工位编号
     */
   private String stationCode = StringUtils.EMPTY;
    /**
     * vin号
     */
   private String vin =  StringUtils.EMPTY;
    /**
     * 车型
     */
   private String vehicleType =  StringUtils.EMPTY;
    /**
     * 拧紧结果
     */
    private Integer tightenResult =  0;
    /**
     * 曲线明细
     */
    private List<LjParamCurveDetailDTO> tightenData = new ArrayList<>();

}
