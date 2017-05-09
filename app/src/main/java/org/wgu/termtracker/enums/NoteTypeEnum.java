package org.wgu.termtracker.enums;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum NoteTypeEnum {
    SELECT(0),
    Text(1),
    Photo(2);

    private static Map<Integer, NoteTypeEnum> map = new HashMap<>();

    private final int type;

    static {
        for (NoteTypeEnum noteTypeEnum : NoteTypeEnum.values()) {
            map.put(noteTypeEnum.type, noteTypeEnum);
        }
    }

    NoteTypeEnum(int type) {
        this.type = type;
    }

    public int getValue() {
        return type;
    }

    @Override
    public String toString() {
        return StringUtils.capitalize(this.name().replaceAll("_", " ").toLowerCase());
    }

    public static NoteTypeEnum valueOf(int type) {
        return map.get(type);
    }
}
