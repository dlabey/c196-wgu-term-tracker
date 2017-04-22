package org.wgu.termtracker.models;

import org.wgu.termtracker.enums.CourseStatusEnum;

import java.util.Date;
import java.util.List;

public class CourseModel {
    private String title;

    private Date startDate;

    private Date anticipatedEndDate;

    private Date dueDate;

    private CourseStatusEnum status;

    private List<CourseMentorModel> mentors;

    private List<NoteModel> notes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getAnticipatedEndDate() {
        return anticipatedEndDate;
    }

    public void setAnticipatedEndDate(Date anticipatedEndDate) {
        this.anticipatedEndDate = anticipatedEndDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public CourseStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CourseStatusEnum status) {
        this.status = status;
    }

    public List<CourseMentorModel> getMentors() {
        return mentors;
    }

    public void setMentors(List<CourseMentorModel> mentors) {
        this.mentors = mentors;
    }

    public List<NoteModel> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteModel> notes) {
        this.notes = notes;
    }
}
