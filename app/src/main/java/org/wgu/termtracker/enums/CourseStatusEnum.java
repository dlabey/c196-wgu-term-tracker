package org.wgu.termtracker.enums;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum CourseStatusEnum {
    SELECT(0),
    IN_PROGRESS(1),
    COMPLETED(2),
    DROPPED(3),
    PLAN_TO_TAKE(4);

    private static Map<Integer, CourseStatusEnum> map = new HashMap<>();

    private final int status;

    static {
        for (CourseStatusEnum courseStatusEnum : CourseStatusEnum.values()) {
            map.put(courseStatusEnum.status, courseStatusEnum);
        }
    }

    CourseStatusEnum(int status) {
        this.status = status;
    }

    public int getValue() {
        return status;
    }

    @Override
    public String toString() {
        return StringUtils.capitalize(this.name().replaceAll("_", " ").toLowerCase());
    }

    public static CourseStatusEnum valueOf(int status) {
        return map.get(status);
    }
}
