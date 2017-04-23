package org.wgu.termtracker.activities;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent(modules = PreferencesModule.class)
public interface PreferencesComponent extends AndroidInjector<PreferencesActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<PreferencesActivity> {
    }
}