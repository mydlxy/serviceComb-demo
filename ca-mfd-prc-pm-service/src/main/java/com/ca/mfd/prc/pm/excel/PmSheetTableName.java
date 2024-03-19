package com.ca.mfd.prc.pm.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author banny.luo
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY banny.luo At 2023年4月28日
 */
public class PmSheetTableName {

    public static final String PM_SHOP = "区域";
    public static final String PM_AVI = "AVI点位";
    public static final String PM_ALL = "汇总";
    public static final String PM_PULL_CORD = "拉绳";
    public static final String PM_OT = "OT屏幕";
    public static final String PM_WO = "工艺";
    public static final String PM_TOOL = "工具";
    public static final String PM_MATERIAL = "物料";
    public static final String PM_TOOL_JOB = "JOB";
    public static final String PM_BOP = "操作指示";
    public static final String PM_BOOK_OPR = "操作指导书";
    public static final String PM_EQUIPMENT_AND_POWER = "设备和设备能力";
    public static final String PM_LINE = "LINE";
    public static final String PM_STATION = "STATION";

    static final List<String> TABLE_NAMES = new ArrayList<>(13);

    static {
        TABLE_NAMES.add(PM_SHOP);
        TABLE_NAMES.add(PM_ALL);
        TABLE_NAMES.add(PM_AVI);
        TABLE_NAMES.add(PM_PULL_CORD);
        TABLE_NAMES.add(PM_OT);
        TABLE_NAMES.add(PM_WO);
        TABLE_NAMES.add(PM_TOOL);
        TABLE_NAMES.add(PM_TOOL_JOB);
        TABLE_NAMES.add(PM_BOP);
        TABLE_NAMES.add(PM_LINE);
        TABLE_NAMES.add(PM_STATION);
        TABLE_NAMES.add(PM_MATERIAL);
        TABLE_NAMES.add(PM_EQUIPMENT_AND_POWER);
        TABLE_NAMES.add(PM_BOOK_OPR);
    }

    public static List<String> getTableNameList() {
        return TABLE_NAMES;
    }
}
