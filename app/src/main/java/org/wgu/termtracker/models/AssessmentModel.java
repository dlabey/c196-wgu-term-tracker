package org.wgu.termtracker.models;

import android.icu.text.SimpleDateFormat;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.enums.AssessmentTypeEnum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AssessmentModel implements Serializable {
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

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        String dueDate = simpleDateFormat.format(this.dueDate);

        return String.format("%s%n%s%nDue %s", title, type.toString() + " Assessment", dueDate);
    }
}
