package com.ca.mfd.prc.pm.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class WoScrewItem {
    private String woCode = StringUtils.EMPTY;
    private String jobNo = StringUtils.EMPTY;
}
