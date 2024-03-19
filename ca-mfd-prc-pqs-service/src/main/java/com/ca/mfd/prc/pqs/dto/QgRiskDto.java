package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class QgRiskDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 问题编号
     */
    private String riskNo = StringUtils.EMPTY;

    /**
     * 问题补充说明
     */
    private String riskRemark = StringUtils.EMPTY;

    /**
     * 缺陷代码
     */
    private String defectAnomalyCode = StringUtils.EMPTY;

    /**
     * 缺陷描述
     */
    private String defectAnomalyDescription = StringUtils.EMPTY;

    /**
     * 是否命中
     */
    private Boolean isActived;

    /**
     * 状态
     */
    private int status;

    /**
     * 发起人
     */
    private String startBy = StringUtils.EMPTY;

    /**
     * 发起时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date startDt;
}
