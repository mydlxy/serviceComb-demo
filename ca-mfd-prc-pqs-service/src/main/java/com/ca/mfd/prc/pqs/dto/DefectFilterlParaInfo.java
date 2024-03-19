package com.ca.mfd.prc.pqs.dto;

import lombok.Data;

/**
 * @Author: joel
 * @Date: 2023-04-10-15:43
 * @Description:
 */
@Data
public class DefectFilterlParaInfo {
    private String key;

    private Integer pageSize = 0;

    private Integer pageIndex = 0;

    private String exsitCodes;
}
