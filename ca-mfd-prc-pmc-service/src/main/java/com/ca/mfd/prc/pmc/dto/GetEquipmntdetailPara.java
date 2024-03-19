package com.ca.mfd.prc.pmc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "")
public class GetEquipmntdetailPara {
    private String shopCode = StringUtils.EMPTY;
    private String startTime = StringUtils.EMPTY;
    private String endTime = StringUtils.EMPTY;
    private String position = StringUtils.EMPTY;
}
