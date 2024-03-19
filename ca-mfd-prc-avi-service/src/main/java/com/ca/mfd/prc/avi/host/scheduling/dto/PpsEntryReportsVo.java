package com.ca.mfd.prc.avi.host.scheduling.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PpsEntryReportsVo implements Serializable {

    private String planNo;

    private int reportType;

    private int entryReportCount;
    
    private String entryReportNo;
}
