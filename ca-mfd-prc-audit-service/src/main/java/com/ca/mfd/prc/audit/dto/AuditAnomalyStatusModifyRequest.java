package com.ca.mfd.prc.audit.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

@Data
public class AuditAnomalyStatusModifyRequest {

    /// <summary>
    /// 缺陷ID
    /// </summary>
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private long dataId = Constant.DEFAULT_ID;

    /// <summary>
    /// 状态
    /// </summary>
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int status = 0;

    /// <summary>
    /// 返修说明，状态2时填写
    /// </summary>
    private AuditRepairActivityRequest repairActivity = new AuditRepairActivityRequest();

    /// <summary>
    /// 备注
    /// </summary>
    private String remark = Strings.EMPTY;
}
