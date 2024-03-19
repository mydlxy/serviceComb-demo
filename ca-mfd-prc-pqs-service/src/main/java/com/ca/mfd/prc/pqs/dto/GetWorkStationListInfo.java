package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

/**
 * @Author: joel
 * @Date: 2023-04-17-15:32
 * @Description:
 */
@Data
public class GetWorkStationListInfo {
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long workstationId = Constant.DEFAULT_ID;

    private String workstationName;
}
