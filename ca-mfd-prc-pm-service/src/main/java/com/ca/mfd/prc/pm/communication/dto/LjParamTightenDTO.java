package com.ca.mfd.prc.pm.communication.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 阳波
 * @ClassName LjParamTightenDTO
 * @description: 拧紧参数信息
 * @date 2023年12月27日
 * @version: 1.0
 */
@Data
public class LjParamTightenDTO {
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
     * 工位拧紧结果
     */
    private Integer jobResult =  0;
    /**
     * 作业结束时间 格式 yyyy-MM-dd HH:mm:ss
     */
    private String jobTime =  StringUtils.EMPTY;

    private List<LjParamTightenDetailDTO> tightenData = new ArrayList<>();

}
