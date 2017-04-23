package org.wgu.termtracker;

import android.app.Activity;

import org.wgu.termtracker.activities.HomeActivity;
import org.wgu.termtracker.activities.HomeComponent;
import org.wgu.termtracker.activities.PreferencesActivity;
import org.wgu.termtracker.activities.PreferencesComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = { HomeComponent.class, PreferencesComponent.class })
abstract class ActivityModule {

    @Binds
    @IntoMap
    @ActivityKey(HomeActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
        bindHomeActivityInjectorFactory(HomeComponent.Builder build);

    @Binds
    @IntoMap
    @ActivityKey(PreferencesActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
        bindPreferencesActivityInjectorFactory(PreferencesComponent.Builder build);
}
