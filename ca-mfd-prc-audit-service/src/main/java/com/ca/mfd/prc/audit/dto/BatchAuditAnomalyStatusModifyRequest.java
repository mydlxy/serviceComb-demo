package com.ca.mfd.prc.audit.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

@Data
public class BatchAuditAnomalyStatusModifyRequest {

    /// <summary>
    /// 缺陷ID
    /// </summary>
    private List<Long> ids;

    /// <summary>
    /// 状态
    /// </summary>
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int status = 2;

    /// <summary>
    /// 返修说明，状态2时填写
    /// </summary>
    private AuditRepairActivityRequest repairActivity = new AuditRepairActivityRequest();

}
