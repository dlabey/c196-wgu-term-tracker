package org.wgu.termtracker.activities;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent(modules = HomeModule.class)
public interface HomeComponent extends AndroidInjector<HomeActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<HomeActivity> {
    }
}
