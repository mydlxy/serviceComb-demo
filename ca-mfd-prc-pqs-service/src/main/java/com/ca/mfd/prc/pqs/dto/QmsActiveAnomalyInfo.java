package com.ca.mfd.prc.pqs.dto;

import lombok.Data;

/**
 * @Author: joel
 * @Date: 2023-04-25-14:55
 * @Description:
 */
@Data
public class QmsActiveAnomalyInfo {
    private String tpsCode;

    private Integer status = 0;
}
