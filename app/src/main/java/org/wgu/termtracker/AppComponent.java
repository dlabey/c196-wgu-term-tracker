package org.wgu.termtracker;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = { AndroidInjectionModule.class, AppModule.class, ActivityModule.class })
interface AppComponent {

    App app();

    void inject(App application);
}