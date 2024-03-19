package com.ca.mfd.prc.avi.host.scheduling.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
public class InsertAsAviPointInfo implements Serializable {

    /**
     * 计划编号
     */
    private String planNo = StringUtils.EMPTY;

    /**
     * 车辆VIN号
     */
    private String vin = StringUtils.EMPTY;


    /**
     * 线体代码
     */
    private String lineCode = StringUtils.EMPTY;


    /**
     * 触发工位
     */
    private String aviCode = StringUtils.EMPTY;

    /**
     * 触发类型(1：正常通过;2：车辆SET OUT;3：车辆SET IN;X：车辆下线;S：车辆上线 )
     */
    private String scanType = "1";

    /**
     * 报工数量（合格）
     */
    private int qualifiedCount = 1;
    /**
     * 报工数量（不合格）
     */
    private int badCount = 0;
}
