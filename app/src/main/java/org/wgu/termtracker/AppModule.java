package org.wgu.termtracker;

import android.content.Context;

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
}
