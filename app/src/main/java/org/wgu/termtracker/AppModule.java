package org.wgu.termtracker;

import android.content.Context;
import android.content.SharedPreferences;

import org.wgu.termtracker.data.PreferencesManager;

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
}
