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
public class QgSetProductRouteInfo {

    /**
     * 唯一码
     */
    private String sn;

    /**
     * 点位地址
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long pointId;

    /**
     * 备注
     */
    private String remark;
}
