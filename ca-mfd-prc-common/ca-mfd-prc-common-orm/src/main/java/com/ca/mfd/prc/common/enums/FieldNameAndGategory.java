package com.ca.mfd.prc.common.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author 阳波
 * @ClassName FieldNameAndGategory
 * @description:
 * @date 2023年09月17日
 * @version: 1.0
 */
public enum FieldNameAndGategory {
    WORKSHOP_CODE("workshopCode","shopCode"),
    LINE_RUN_TYPE("runType","LineRunModel"),
    LINE_TYPE("lineType","LineType"),
    STATION_TYPE("workstationType","workstaionType"),
    STATION_TEAM("teamNo","team"),
    STATION_DIRECTION("direction","workPlaceCode"),
    TOOL_TYPE("toolType","toolType"),
    TOOL_NET_TYPE("netType","toolCommType"),
    TOOL_BRAND("brand","toolBrand"),
    WO_TYPE("woType","woType"),
    WO_OPER_TYPE("operType","WoOperType"),
    PULL_CORD_TYPE("type","pullcord"),
    PULL_STOP_TYPE("stopType","pullcordStopType"),

    AVI_TYPE("aviType","aviType"),
    AVI_ATTR("aviAttribute","Avifeatures"),
    AVI_FUNC("aviFunctions","aviFunction"),
    AVI_DEFAULT_PAGE("defaultPage","aviFunction"),
    EQUIPMENT_POWER_TYPE("powerType","equipmentPower"),
    EQUIPMENT_POWER_UNIT("unit","equipmentPowerUnit"),
    OT_TEMPLATE("template","OTTemplate");


    private final String fieldName;
    private final String gategoryName;

    FieldNameAndGategory(String fieldName, String gategoryName) {
        this.fieldName = fieldName;
        this.gategoryName = gategoryName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getGategoryName() {
        return gategoryName;
    }

    public static String getGategoryName(String fieldName){
        FieldNameAndGategory[] fieldNameAndGategorys = FieldNameAndGategory.values();
        FieldNameAndGategory fieldNameAndGategory = Arrays.stream(fieldNameAndGategorys).filter(item -> item.fieldName.equalsIgnoreCase(fieldName))
                .findFirst().orElse(null);
        if(fieldNameAndGategory != null){
            return fieldNameAndGategory.getGategoryName();
        }
        return StringUtils.EMPTY;
    }


}
