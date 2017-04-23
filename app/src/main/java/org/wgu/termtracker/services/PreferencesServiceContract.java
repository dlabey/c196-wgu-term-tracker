package org.wgu.termtracker.services;

import org.wgu.termtracker.models.PreferencesModel;

public interface PreferencesServiceContract {

    public PreferencesModel getPreferences();

    public boolean setPreferences();
}
