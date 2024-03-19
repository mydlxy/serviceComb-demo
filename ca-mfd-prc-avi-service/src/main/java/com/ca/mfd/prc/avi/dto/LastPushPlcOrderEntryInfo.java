package com.ca.mfd.prc.avi.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 保存最后一次信息
 *
 * @Author: joel
 * @Date: 2023-08-16-16:47
 * @Description:
 */
@Data
public class LastPushPlcOrderEntryInfo {
    private String id = StringUtils.EMPTY;

    private String lastSn = StringUtils.EMPTY;

    private String lastVin = StringUtils.EMPTY;


}
