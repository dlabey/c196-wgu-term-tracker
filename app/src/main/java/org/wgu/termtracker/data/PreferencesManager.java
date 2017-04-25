package org.wgu.termtracker.data;

import org.wgu.termtracker.enums.SharingMethodEnum;
import org.wgu.termtracker.models.PreferencesModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesManager implements PreferencesContract {

    @Inject
    PreferencesManager() {
    }

    @Override
    public PreferencesModel getPreferences() {
        return new PreferencesModel();
    }

    @Override
    public boolean setPreferences(boolean isCourseAlerts, int courseAlertMinutes,
                                  boolean isAssessmentAlerts, int assessmentAlertMinutes,
                                  SharingMethodEnum sharingMethod) {
        return true;
    }
}
