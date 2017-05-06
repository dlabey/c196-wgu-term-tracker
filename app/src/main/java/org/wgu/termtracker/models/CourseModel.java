package org.wgu.termtracker.models;

import android.icu.text.SimpleDateFormat;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.enums.CourseStatusEnum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CourseModel implements Serializable {
    private long courseId;

    private String title;

    private Date startDate;

    private Date anticipatedEndDate;

    private Date dueDate;

    private CourseStatusEnum status;

    private List<CourseMentorModel> mentors;

    private List<NoteModel> notes;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

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

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        String startDate = simpleDateFormat.format(this.startDate);
        String dueDate = simpleDateFormat.format(this.dueDate);

        return String.format("%s%n%s - (Due) %s", title, startDate, dueDate);
    }
}
