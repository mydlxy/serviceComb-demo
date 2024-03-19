package com.ca.mfd.prc.common.dto.pmc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * PackagingPointResponse
 *
 * @author inkelink
 * @date 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PackagingPointResponse {
    private String area = StringUtils.EMPTY;
    private String position = StringUtils.EMPTY;
    private String station = StringUtils.EMPTY;
    private String eqCode = StringUtils.EMPTY;
    private String alarmCode = StringUtils.EMPTY;
    private String alarmDes = StringUtils.EMPTY;
    private String alarmLevel = StringUtils.EMPTY;
}
