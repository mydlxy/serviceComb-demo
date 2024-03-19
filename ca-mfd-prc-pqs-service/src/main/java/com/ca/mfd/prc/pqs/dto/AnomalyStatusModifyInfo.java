package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

/**
 * @Author: qu
 * @Date:
 */
@Data
public class AnomalyStatusModifyInfo {

    /**
     * 缺陷ID
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long dataId = Constant.DEFAULT_ID;

    /**
     * 状态
     */
    private int status;

    /**
     * 返修说明，状态2时填写
     */
    private RepairActivity repairActivity;

    /**
     * 备注
     */
    private String remark;
}
