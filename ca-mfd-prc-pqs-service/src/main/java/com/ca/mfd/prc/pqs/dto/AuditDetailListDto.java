package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class AuditDetailListDto {

    /**
     * 评审单号
     */
    private String inspectionNo = StringUtils.EMPTY;

    /**
     * 工位代码
     */
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 工位名称
     */
    private String workstaionName = StringUtils.EMPTY;

    /**
     * 操作人
     */
    private String qcUser = StringUtils.EMPTY;

    /**
     * 质检时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date qcDt = new Date();

    /**
     * 物料号
     */
    private String materialNo = StringUtils.EMPTY;


    /**
     * 物料描述
     */
    private String materialCn = StringUtils.EMPTY;

    /**
     * 处置方式
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int handleWay = 0;

    /**
     * 梳理
     */
    private BigDecimal qty = BigDecimal.valueOf(0);

    /**
     * 单位
     */
    private String unit = StringUtils.EMPTY;
}
