package org.wgu.termtracker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.wgu.termtracker.R;

import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class TermInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_term_input);
        ButterKnife.bind(this);
    }
}
