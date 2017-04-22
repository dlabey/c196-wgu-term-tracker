package org.wgu.termtracker.enums;

public enum AsessmentTypeEnum {
    Performance(1),
    Objective(2);

    private final int type;

    AsessmentTypeEnum(int type) {
        this.type = type;
    }

    public int getValue() {
        return type;
    }
}
