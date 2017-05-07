package org.wgu.termtracker.activities;

import org.wgu.termtracker.AppModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ContributesAndroidInjectorModule {
    @ContributesAndroidInjector
    public abstract HomeActivity contributeHomeActivityInjector();

    @ContributesAndroidInjector
    public abstract PreferencesActivity contributePreferencesActivityInjector();

    @ContributesAndroidInjector
    public abstract TermListActivity contributeTermListActivity();

    @ContributesAndroidInjector
    public abstract TermInputActivity contributeTermInputActivity();

    @ContributesAndroidInjector
    public abstract TermViewActivity contributeTermViewActivity();

    @ContributesAndroidInjector
    public abstract CourseInputActivity contributeCourseInputActivity();

    @ContributesAndroidInjector
    public abstract CourseViewActivity contributeCourseViewActivity();

    @ContributesAndroidInjector
    public abstract CourseMentorInputActivity contributeCourseMentorInputActivity();

    @ContributesAndroidInjector
    public abstract CourseMentorViewActivity contributeCourseMentorViewActivity();

    @ContributesAndroidInjector
    public abstract AssessmentInputActivity contributeAssessmentInputActivity();

    @ContributesAndroidInjector
    public abstract AssessmentViewActivity contributeAssessmentViewActivity();
}
