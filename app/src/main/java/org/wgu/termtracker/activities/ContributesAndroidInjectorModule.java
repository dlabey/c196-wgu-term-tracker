package org.wgu.termtracker.activities;

import org.wgu.termtracker.AppModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ContributesAndroidInjectorModule {
    @ContributesAndroidInjector(modules = {})
    public abstract HomeActivity contributeHomeActivityInjector();

    @ContributesAndroidInjector(modules = {})
    public abstract PreferencesActivity contributePreferencesActivityInjector();
}
