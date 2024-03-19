package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class QgAnomalyAppendRemarkInfo {

    /**
     * 缺陷ID
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long dataId;

    /**
     * 备注
     */
    private String remark;
}
