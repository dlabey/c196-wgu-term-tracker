package org.wgu.termtracker;

import android.app.Application;

import org.wgu.termtracker.activities.HomeActivity;

import javax.inject.Singleton;

import dagger.Component;

public class TermTrackerApplication extends Application {
    @Singleton
    @Component(modules = AndroidModule.class)
    public interface ApplicationComponent {
        void inject(TermTrackerApplication application);
        void inject(HomeActivity homeActivity);
    }

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerTermTrackerApplication_ApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();
        component().inject(this);
    }

    public ApplicationComponent component() {
        return component;
    }
}
