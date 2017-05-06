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

    @ContributesAndroidInjector(modules = {})
    public abstract TermListActivity contributeTermListActivity();

    @ContributesAndroidInjector(modules = {})
    public abstract TermInputActivity contributeTermInputActivity();

    @ContributesAndroidInjector(modules = {})
    public abstract TermViewActivity contributeTermViewActivity();

    @ContributesAndroidInjector(modules = {})
    public abstract CourseInputActivity contributeCourseInputActivity();
}
