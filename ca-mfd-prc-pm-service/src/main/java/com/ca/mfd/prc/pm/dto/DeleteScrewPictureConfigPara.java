package com.ca.mfd.prc.pm.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class DeleteScrewPictureConfigPara {
    private String jobNo = StringUtils.EMPTY;
    private Long pmWoId = 0L;
}
