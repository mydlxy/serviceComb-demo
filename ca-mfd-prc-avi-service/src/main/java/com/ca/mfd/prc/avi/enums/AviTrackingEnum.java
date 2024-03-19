package com.ca.mfd.prc.avi.enums;

import java.util.Arrays;

/**
 * ConditionOper enum
 *
 * @author cwy
 * @date 2023/4/7
 */
public enum AviTrackingEnum {


    自动过点(1, "自动过点"),

    手动过点(2, "手动过点"),

    Plc手过点(3, "Plc手过点");


    private final int value;
    private final String description;

    AviTrackingEnum(int value, String description) {

        this.value = value;
        this.description = description;
    }

    public static AviTrackingEnum fromInt(Integer val, AviTrackingEnum def) {
        if (val == null) {
            return def;
        }
        return Arrays.stream(AviTrackingEnum.values()).filter(c -> c.value() == val).findFirst().orElse(def);
    }

    public int value() {
        return this.value;
    }

    public String description() {
        return this.description;
    }
}
