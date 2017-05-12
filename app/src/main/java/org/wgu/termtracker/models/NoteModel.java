package org.wgu.termtracker.models;

import java.io.Serializable;

public class NoteModel implements Serializable {
    private long noteId;

    private String text;

    private String photoUri;

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
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

    @Override
    public String toString() {
        return String.format("%s", text);
    }
}
