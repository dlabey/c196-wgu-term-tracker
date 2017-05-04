package org.wgu.termtracker.models;

import org.wgu.termtracker.enums.AssessmentTypeEnum;

import java.util.Date;
import java.util.List;

public class AssessmentModel {
    private long assessmentId;

    private String title;

    private Date dueDate;

    private AssessmentTypeEnum type;

    private List<NoteModel> notes;

    public long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public AssessmentTypeEnum getType() {
        return type;
    }

    public void setType(AssessmentTypeEnum type) {
        this.type = type;
    }

    public List<NoteModel> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteModel> notes) {
        this.notes = notes;
    }
}
