package com.ca.mfd.prc.eps.enums;

/**
 * WorkStationType
 *
 * @author inkelink
 * @date 2023/09/05
 */
public enum WorkStationType {
    PRODUCTION(1),
    QUALITY(2),
    REPAIR(3),
    ASSEMBLY(4);

    private final int value;

    WorkStationType(int value) {
        this.value = value;
    }

    public static WorkStationType fromValue(int value) {
        for (WorkStationType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid workstation type value: " + value);
    }

    public int getValue() {
        return value;
    }
}
