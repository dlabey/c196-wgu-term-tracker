package org.wgu.termtracker.models;

import org.wgu.termtracker.enums.NoteTypeEnum;

public class NoteModel {
    private NoteTypeEnum type;

    private String text;

    private String filename;

    public NoteTypeEnum getType() {
        return type;
    }

    public void setType(NoteTypeEnum type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
