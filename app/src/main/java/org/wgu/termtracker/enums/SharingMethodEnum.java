package org.wgu.termtracker.enums;

import java.util.HashMap;
import java.util.Map;

public enum SharingMethodEnum {
    EMAIL(1),
    SMS(2);

    private static Map<Integer, SharingMethodEnum> map = new HashMap<>();

    private final int method;

    static {
        for (SharingMethodEnum sharingMethodEnum : SharingMethodEnum.values()) {
            map.put(sharingMethodEnum.method, sharingMethodEnum);
        }
    }

    SharingMethodEnum(int method) {
        this.method = method;
    }

    public int getValue() {
        return method;
    }

    public static SharingMethodEnum valueOf(int method) {
        return map.get(method);
    }
}
