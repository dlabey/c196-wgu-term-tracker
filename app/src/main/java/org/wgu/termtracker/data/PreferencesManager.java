package org.wgu.termtracker.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.wgu.termtracker.models.PreferencesModel;

import javax.inject.Singleton;

@Singleton
public class PreferencesManager implements PreferencesContract {

    private static String IS_COURSE_ALERTS = "is_course_alerts";

    private static String COURSE_ALERT_DAYS = "course_alert_days";

    private static String IS_ASSESSMENT_ALERTS = "is_assessment_alerts";

    private static String ASSESSMENT_ALERT_DAYS = "assessment_alert_days";

    private SharedPreferences sharedPrefs;

    public PreferencesManager(Context context) {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public PreferencesModel getPreferences() {
        boolean isCourseAlerts = this.sharedPrefs.getBoolean(IS_COURSE_ALERTS, true);
        int courseAlertMinutes = this.sharedPrefs.getInt(COURSE_ALERT_DAYS, 7);
        boolean isAssessmentAlerts = this.sharedPrefs.getBoolean(IS_ASSESSMENT_ALERTS, true);
        int assessmentAlertMinutes = this.sharedPrefs.getInt(ASSESSMENT_ALERT_DAYS, 7);

        PreferencesModel preferences = new PreferencesModel();
        preferences.setCourseAlerts(isCourseAlerts);
        preferences.setCourseAlertDays(courseAlertMinutes);
        preferences.setAssessmentAlerts(isAssessmentAlerts);
        preferences.setAssessmentAlertDays(assessmentAlertMinutes);

        return preferences;
    }

    @Override
    public boolean setPreferences(boolean isCourseAlerts, int courseAlertMinutes,
                                  boolean isAssessmentAlerts, int assessmentAlertMinutes) {
        SharedPreferences.Editor sharedPrefsEditor = this.sharedPrefs.edit();

        sharedPrefsEditor.putBoolean(IS_COURSE_ALERTS, isCourseAlerts);
        sharedPrefsEditor.putInt(COURSE_ALERT_DAYS, courseAlertMinutes);
        sharedPrefsEditor.putBoolean(IS_ASSESSMENT_ALERTS, isAssessmentAlerts);
        sharedPrefsEditor.putInt(ASSESSMENT_ALERT_DAYS, assessmentAlertMinutes);

        return sharedPrefsEditor.commit();
    }
}
