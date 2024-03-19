package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class QgRiskOperInfo {

    /**
     * 围堵明细ID
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 操作类型 20激活 90释放
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int operType = 20;

    /**
     * 操作工位
     */
    private String workstationCode;

    /**
     * 释放备注
     */
    private String closeRemark;

    /**
     * 返修说明，状态2时填写
     */
    private RepairActivity repairActivity;
}
