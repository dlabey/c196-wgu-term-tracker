package org.wgu.termtracker.enums;

import java.util.HashMap;
import java.util.Map;

public enum AssessmentTypeEnum {
    Performance(1),
    Objective(2);

    private static Map<Integer, AssessmentTypeEnum> map = new HashMap<>();

    private final int type;

    static {
        for (AssessmentTypeEnum assessmentTypeEnum : AssessmentTypeEnum.values()) {
            map.put(assessmentTypeEnum.type, assessmentTypeEnum);
        }
    }

    AssessmentTypeEnum(int type) {
        this.type = type;
    }

    public int getValue() {
        return type;
    }

    public static AssessmentTypeEnum valueOf(int type) {
        return map.get(type);
    }
}
