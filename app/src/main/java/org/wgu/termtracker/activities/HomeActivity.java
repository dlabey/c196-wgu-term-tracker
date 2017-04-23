package org.wgu.termtracker.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.wgu.termtracker.R;

import dagger.android.AndroidInjection;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    protected void onPreferencesClick(View view) {
        Intent intent = new Intent(this, PreferencesActivity.class);

        startActivity(intent);
    }

    protected void onManageTermsClick(View view) {
        Intent intent = new Intent(this, TermListActivity.class);

        startActivity(intent);
    }
}
