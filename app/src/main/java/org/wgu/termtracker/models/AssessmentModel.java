package org.wgu.termtracker.models;

import org.wgu.termtracker.enums.AsessmentTypeEnum;

import java.util.Date;

public class AssessmentModel {
    private String title;

    private Date dueDate;

    private AsessmentTypeEnum type;

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

    public AsessmentTypeEnum getType() {
        return type;
    }

    public void setType(AsessmentTypeEnum type) {
        this.type = type;
    }
}
