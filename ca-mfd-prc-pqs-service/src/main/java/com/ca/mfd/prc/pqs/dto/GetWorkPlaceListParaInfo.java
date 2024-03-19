package com.ca.mfd.prc.pqs.dto;

import lombok.Data;

/**
 * @Author: joel
 * @Date: 2023-04-17-15:15
 * @Description:
 */
@Data
public class GetWorkPlaceListParaInfo {
    private String workPlaceName;

    private Integer pageSize = 0;

    private Integer pageIndex = 0;
}
