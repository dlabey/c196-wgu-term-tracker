package org.wgu.termtracker.enums;

public enum AssessmentTypeEnum {
    Performance(1),
    Objective(2);

    private final int type;

    AssessmentTypeEnum(int type) {
        this.type = type;
    }

    public int getValue() {
        return type;
    }
}
