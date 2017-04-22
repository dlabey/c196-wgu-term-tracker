package org.wgu.termtracker.models;

import org.wgu.termtracker.enums.NoteTypeEnum;

public class NoteModel {
    private NoteTypeEnum type;

    private String text;

    private String fileName;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
