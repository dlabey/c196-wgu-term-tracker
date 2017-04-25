package org.wgu.termtracker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.wgu.termtracker.App;
import org.wgu.termtracker.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class PreferencesActivity extends AppCompatActivity {

    private static final String TAG = "PreferencesActivity";

    @Inject
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preferences);
    }
}
