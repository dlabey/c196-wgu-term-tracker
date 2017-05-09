package org.wgu.termtracker.models;

import org.wgu.termtracker.enums.NoteTypeEnum;

public class NoteModel {
    private long noteId;

    private NoteTypeEnum type;

    private String text;

    private String photoUri;

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

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

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
