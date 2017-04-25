package org.wgu.termtracker;

import org.wgu.termtracker.activities.ContributesAndroidInjectorModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        ContributesAndroidInjectorModule.class
})
interface AppComponent extends AndroidInjector<App> {
    App app();

    void inject(App application);
}