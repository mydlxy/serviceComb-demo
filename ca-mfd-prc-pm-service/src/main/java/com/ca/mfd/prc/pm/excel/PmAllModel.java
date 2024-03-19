package com.ca.mfd.prc.pm.excel;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;

/**
 * 导出工厂建模报表用的汇总实体类
 */
@Data
public class PmAllModel {

    private String workshopCode = Strings.EMPTY;

    private String workshopName = Strings.EMPTY;

    private Integer workshopDesignJph = 0;

    private Integer workshopProductTime = 0;

    private String lineCode = Strings.EMPTY;

    private String lineName = Strings.EMPTY;

    private Integer stationCount = 0;

    private String lineType = Strings.EMPTY;

    private String runType = Strings.EMPTY;

    private Integer stationLength = 0;

    private Integer lineLength = 0;

    private Integer lineDesignJph = 0;

    private Integer lineProductTime = 0;

    private String opcConnect = Strings.EMPTY;

    private String queueDb = Strings.EMPTY;

    private String stationDb = Strings.EMPTY;

    private String andonOpcConnect = Strings.EMPTY;

    private String andonDb = Strings.EMPTY;

    private Boolean lineIsEnable = false;

    private Boolean lineIsDelete = false;

    private String workstationCode = Strings.EMPTY;

    private String workstationName = Strings.EMPTY;

    private String workstationType = Strings.EMPTY;

    private String workstationNo = Strings.EMPTY;

    private String direction = Strings.EMPTY;

    private String teamNo = Strings.EMPTY;

    private Integer beginDistance = 0;

    private Integer endDistance = 0;

    private Integer alarmDistance = 0;

    private Integer workstationProductTime = 0;

    private Boolean stationIsEnable = false;

    private Boolean stationIsDelete = false;

    private String remark = Strings.EMPTY;
}
