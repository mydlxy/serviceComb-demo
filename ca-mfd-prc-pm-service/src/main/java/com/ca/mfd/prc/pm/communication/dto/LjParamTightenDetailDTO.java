package com.ca.mfd.prc.pm.communication.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 阳波
 * @ClassName LjParamTightenDetailDTO
 * @description: 拧紧参数明细
 * @date 2023年12月27日
 * @version: 1.0
 */
@Data
public class LjParamTightenDetailDTO {
    /**
     *工具编码
     */
    private String toolId = StringUtils.EMPTY;
    /**
     * 控制器名称(25 char)
     */
    private String controllerName = StringUtils.EMPTY;
    /**
     *VIN条码(25 char)
     */
    private String vin = StringUtils.EMPTY;
    /**
     * JOB号(0-9999)
     */
    private Integer jobId = 0;
    /**
     * 程序号(0-999)
     */
    private Integer pSetId = 0;
    /**
     * 批次数(0-99)
     */
    private Integer batchSize = 0;
    /**
     * 拧紧序号(0-99)
     */
    private Integer batchCounter = 0;
    /**
     * 螺栓结果(0=NG，1=OK)
     */
    private Integer tighteningstatus = 0;
    /**
     * JOB结果(0=NOK，1=OK，2=Not use)
     */
    private Integer batchstatus = 0;
    /**
     * 扭矩结果(0=Low, 1=OK, 2=High)
     */
    private Integer torquestatus = 0;
    /**
     * 角度结果(0=Low, 1=OK, 2=High)
     */
    private Integer anglestatus = 0;
    /**
     * 扭力上限(0.01-9999.99)
     */
    private Double torqueMax = 0D;
    /**
     * 扭力下限(0.01-9999.99)
     */
    private Double torqueMin = 0D;
    /**
     * 目标扭矩(0.01-9999.99)
     */
    private Double torqueTarget = 0D;
    /**
     * 实际扭矩(0.01-9999.99)
     */
    private Double torque = 0D;
    /**
     * 角度上限(0-99999.99)
     */
    private Double angleMax = 0D;
    /**
     * 角度下限(0-99999.99)
     */
    private Double angleMin = 0D;
    /**
     * 目标角度(0-99999.99)
     */
    private Double angleTarget = 0D;
    /**
     * 实际角度(0-99999.99)
     */
    private Double angle = 0D;
    /**
     * 拧紧ID(max 4294967295)
     */
    private Long tighteningID = 0L;
    /**
     * 拧紧时间(yyyy-MM-dd HH:mm:ss)
     */
    private String tighteningTime = StringUtils.EMPTY;

}
