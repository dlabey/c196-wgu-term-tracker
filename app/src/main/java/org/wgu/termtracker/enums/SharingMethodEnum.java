package org.wgu.termtracker.enums;

public enum SharingMethodEnum {
    EMAIL(1),
    SMS(2);

    private final int method;

    SharingMethodEnum(int method) {
        this.method = method;
    }

    public int getValue() {
        return method;
    }
}
