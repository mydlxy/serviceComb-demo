package com.ca.mfd.prc.pqs.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: joel
 * @Date: 2023-08-18-11:51
 * @Description:
 */
@Data
public class UpdateDutyAreaInfo {
    private List<Long> vehicleDefectAnomalyIds;

    private String dutyArea;

    private String dutyAreaRemark;
}
