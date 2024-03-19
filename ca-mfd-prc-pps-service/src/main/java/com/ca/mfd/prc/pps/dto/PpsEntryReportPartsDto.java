package com.ca.mfd.prc.pps.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PpsEntryReportPartsDto implements Serializable {
    /**
     * 分类
     */
    private Integer orderCategory;

    /**
     * SN 集合
     */
    private List<String> rprtNos;
}
