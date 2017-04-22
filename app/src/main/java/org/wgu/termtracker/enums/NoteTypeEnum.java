package org.wgu.termtracker.enums;

public enum NoteTypeEnum {
    Text(1),
    File(2);

    private final int type;

    NoteTypeEnum(int type) {
        this.type = type;
    }

    public int getValue() {
        return type;
    }
}
