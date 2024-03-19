package com.ca.mfd.prc.pqs.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: joel
 * @Date: 2023-04-17-19:07
 * @Description:
 */
@Data
public class GetGateAnomalyListInfo {
    private String id = StringUtils.EMPTY;

    private String componentCode;

    private String positionCode;
}
