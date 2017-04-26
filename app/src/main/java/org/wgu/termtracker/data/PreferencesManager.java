package org.wgu.termtracker.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.wgu.termtracker.enums.SharingMethodEnum;
import org.wgu.termtracker.models.PreferencesModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesManager implements PreferencesContract {

    private static String IS_COURSE_ALERTS = "is_course_alerts";

    private static String COURSE_ALERT_MINUTES = "course_alert_minutes";

    private static String IS_ASSESSMENT_ALERTS = "is_assessment_alerts";

    private static String ASSESSMENT_ALERT_MINUTES = "assessment_alert_minutes";

    private static String SHARING_METHOD = "sharing_method";

    private Application app;

    private SharedPreferences sharedPrefs;

    public PreferencesManager(Application app) {
        this.app = app;

        sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.app.getBaseContext());
    }

    @Override
    public PreferencesModel getPreferences() {
        boolean isCourseAlerts = this.sharedPrefs.getBoolean(IS_COURSE_ALERTS, true);
        int courseAlertMinutes = this.sharedPrefs.getInt(COURSE_ALERT_MINUTES, 10);
        boolean isAssessmentAlerts = this.sharedPrefs.getBoolean(IS_ASSESSMENT_ALERTS, true);
        int assessmentAlertMinutes = this.sharedPrefs.getInt(ASSESSMENT_ALERT_MINUTES, 10);
        SharingMethodEnum sharingMethod = SharingMethodEnum.valueOf(this.sharedPrefs
                .getInt(SHARING_METHOD, SharingMethodEnum.EMAIL.getValue()));

        PreferencesModel preferences = new PreferencesModel();
        preferences.setCourseAlerts(isCourseAlerts);
        preferences.setCourseAlertMinutes(courseAlertMinutes);
        preferences.setAssessmentAlerts(isAssessmentAlerts);
        preferences.setAssessmentAlertMinutes(assessmentAlertMinutes);
        preferences.setSharingMethod(sharingMethod);

        return preferences;
    }

    @Override
    public boolean setPreferences(boolean isCourseAlerts, int courseAlertMinutes,
                                  boolean isAssessmentAlerts, int assessmentAlertMinutes,
                                  SharingMethodEnum sharingMethod) {
        SharedPreferences.Editor sharedPrefsEditor = this.sharedPrefs.edit();

        sharedPrefsEditor.putBoolean(IS_COURSE_ALERTS, isCourseAlerts);
        sharedPrefsEditor.putInt(COURSE_ALERT_MINUTES, courseAlertMinutes);
        sharedPrefsEditor.putBoolean(IS_ASSESSMENT_ALERTS, isAssessmentAlerts);
        sharedPrefsEditor.putInt(ASSESSMENT_ALERT_MINUTES, assessmentAlertMinutes);
        sharedPrefsEditor.putInt(SHARING_METHOD, sharingMethod.getValue());

        return sharedPrefsEditor.commit();
    }
}
