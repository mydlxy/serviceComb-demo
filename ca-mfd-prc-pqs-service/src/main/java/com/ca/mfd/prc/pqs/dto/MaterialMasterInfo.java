package com.ca.mfd.prc.pqs.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: joel
 * @Date: 2023-04-12-20:04
 * @Description:
 */
@Data
public class MaterialMasterInfo {

    private String materialMasterId = StringUtils.EMPTY;

    private String materialNo;
}
