package org.wgu.termtracker.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.wgu.termtracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class TermListActivity extends AppCompatActivity {

    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @BindView(R.id.termListView)
    ListView termList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_term_list);
        ButterKnife.bind(this);

        setSupportActionBar(actionBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.term_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTerm:
                this.onAddTermClick(item.getActionView());
                break;
        }

        return false;
    }

    protected void onAddTermClick(View view) {
        Intent intent = new Intent(this, TermInputActivity.class);

        startActivity(intent);
    }
}
