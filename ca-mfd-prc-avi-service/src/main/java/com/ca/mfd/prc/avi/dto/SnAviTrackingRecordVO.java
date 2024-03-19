package com.ca.mfd.prc.avi.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class SnAviTrackingRecordVO implements Serializable {

    private Date insertDt;
    private String sn = StringUtils.EMPTY;
    private String pmAviCode = StringUtils.EMPTY;
}
