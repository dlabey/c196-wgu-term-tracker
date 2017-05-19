package org.wgu.termtracker.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.wgu.termtracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(HomeActivity.this);

        setSupportActionBar(actionBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences:
                this.onPreferencesClick(item.getActionView());
                break;
        }

        return false;
    }

    public void onPreferencesClick(View view) {
        Intent intent = new Intent(this, PreferencesActivity.class);

        startActivity(intent);
    }

    public void onManageTermsClick(View view) {
        Intent intent = new Intent(this, TermListActivity.class);

        startActivity(intent);
    }
}
