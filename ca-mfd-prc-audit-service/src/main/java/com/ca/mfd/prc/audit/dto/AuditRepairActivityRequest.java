package com.ca.mfd.prc.audit.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AuditRepairActivityRequest {
    /// <summary>
    /// 返修方式
    /// </summary>
    private String repairWay = Strings.EMPTY;

    /// <summary>
    /// 返修时间
    /// </summary>
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date repairTime = new Date();


    /// <summary>
    /// 返修工时
    /// </summary>
    private BigDecimal spendTime = BigDecimal.valueOf(0);

    /// <summary>
    /// 返修备注
    /// </summary>
    private String repairRemark = Strings.EMPTY;
}
