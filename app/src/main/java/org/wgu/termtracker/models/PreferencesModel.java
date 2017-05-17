package org.wgu.termtracker.models;

public class PreferencesModel {
    private boolean isCourseAlerts;

    private int courseAlertDays;

    private boolean isAssessmentAlerts;

    private int assessmentAlertDays;

    public boolean isCourseAlerts() {
        return isCourseAlerts;
    }

    public void setCourseAlerts(boolean courseAlerts) {
        isCourseAlerts = courseAlerts;
    }

    public int getCourseAlertDays() {
        return courseAlertDays;
    }

    public void setCourseAlertDays(int courseAlertDays) {
        this.courseAlertDays = courseAlertDays;
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
}
