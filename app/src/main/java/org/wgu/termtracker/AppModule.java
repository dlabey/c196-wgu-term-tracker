package org.wgu.termtracker;

import android.content.Context;
import android.content.SharedPreferences;

import org.wgu.termtracker.data.AssessmentManager;
import org.wgu.termtracker.data.CourseManager;
import org.wgu.termtracker.data.CourseMentorManager;
import org.wgu.termtracker.data.NoteManager;
import org.wgu.termtracker.data.PreferencesManager;
import org.wgu.termtracker.data.TermManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final App app;

    AppModule(App app) {
        this.app = app;
    }

    @Singleton
    @Provides
    App provideApp() {
        return app;
    }

    @Singleton
    @Provides
    PreferencesManager providePreferencesManager() {
        return new PreferencesManager(app);
    }

    @Singleton
    @Provides
    TermManager provideTermManager() { return new TermManager(app.getBaseContext()); }

    @Singleton
    @Provides
    CourseManager provideCourseManager() { return new CourseManager(app.getBaseContext()); }

    @Singleton
    @Provides
    CourseMentorManager provideCourseMentorManager() {
        return new CourseMentorManager(app.getBaseContext());
    }

    @Singleton
    @Provides
    AssessmentManager provideAssessmentManager() {
        return new AssessmentManager(app.getBaseContext());
    }

    @Singleton
    @Provides
    NoteManager provideNoteManager() {
        return new NoteManager(app.getBaseContext());
    }
}
