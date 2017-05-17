package org.wgu.termtracker.data;

import org.wgu.termtracker.models.PreferencesModel;

import javax.inject.Singleton;

@Singleton
interface PreferencesContract {

    PreferencesModel getPreferences();

    boolean setPreferences(boolean isCourseAlerts, int courseAlertMinutes,
                           boolean isAssessmentAlerts, int assessmentAlertMinutes);
}
