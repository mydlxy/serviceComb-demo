package com.ca.mfd.prc.pqs.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: joel
 * @Date: 2023-04-12-16:53
 * @Description:
 */
@Data
public class MaintainDataParaInfo {

    private String workstationCode;

    private List<DefectAnomalyDto> defectAnomalyList;
}
