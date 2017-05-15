package org.wgu.termtracker.models;

import org.wgu.termtracker.enums.SharingMethodEnum;

public class PreferencesModel {
    private boolean isCourseAlerts;

    private int courseAlertMinutes;

    private boolean isAssessmentAlerts;

    private int assessmentAlertDays;

    private SharingMethodEnum sharingMethod;

    public boolean isCourseAlerts() {
        return isCourseAlerts;
    }

    public void setCourseAlerts(boolean courseAlerts) {
        isCourseAlerts = courseAlerts;
    }

    public int getCourseAlertMinutes() {
        return courseAlertMinutes;
    }

    public void setCourseAlertMinutes(int courseAlertMinutes) {
        this.courseAlertMinutes = courseAlertMinutes;
    }

    public boolean isAssessmentAlerts() {
        return isAssessmentAlerts;
    }

    public void setAssessmentAlerts(boolean assessmentAlerts) {
        isAssessmentAlerts = assessmentAlerts;
    }

    public int getAssessmentAlertDays() {
        return assessmentAlertDays;
    }

    public void setAssessmentAlertDays(int assessmentAlertDays) {
        this.assessmentAlertDays = assessmentAlertDays;
    }

    public SharingMethodEnum getSharingMethod() {
        return sharingMethod;
    }

    public void setSharingMethod(SharingMethodEnum sharingMethod) {
        this.sharingMethod = sharingMethod;
    }
}
