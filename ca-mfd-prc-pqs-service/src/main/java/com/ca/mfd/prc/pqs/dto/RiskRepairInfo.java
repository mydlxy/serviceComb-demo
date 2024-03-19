package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class RiskRepairInfo {

    /**
     * ID集合
     */
    private List<Long> ids;

    /**
     * 返修说明，状态2时填写
     */
    private RepairActivity repairActivity;

    /**
     * 状态
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int status = 2;

    /**
     * 关闭说明
     */
    private String closeRemark = StringUtils.EMPTY;
}
