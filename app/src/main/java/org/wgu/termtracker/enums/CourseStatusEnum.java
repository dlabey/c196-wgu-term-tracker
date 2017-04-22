package org.wgu.termtracker.enums;

public enum CourseStatusEnum {
    IN_PROGRESS(1),
    COMPLETED(2),
    DROPPED(3),
    PLAN_TO_TAKE(4);

    private final int state;

    CourseStatusEnum(int state) {
        this.state = state;
    }

    public int getValue() {
        return state;
    }
}
