package com.ca.mfd.prc.pqs.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class PqsRouteDto {

    /**
     * 记录编号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dataId;

    /**
     * 工位
     */
    private String workstationCode;

    /**
     * 工位名称
     */
    private String workstationName;

    /**
     * 区域代码
     */
    private String areaCode;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 调度代码
     */
    private String rcCode;

    /**
     * 备注
     */
    private String remark;
}
