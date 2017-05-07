package org.wgu.termtracker.enums;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum AssessmentTypeEnum {
    SELECT(0),
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

    @Override
    public String toString() {
        return StringUtils.capitalize(this.name().replaceAll("_", " ").toLowerCase());
    }

    public static AssessmentTypeEnum valueOf(int type) {
        return map.get(type);
    }
}
