package org.wgu.termtracker.activities;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.WrapperListAdapter;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.R;
import org.wgu.termtracker.data.TermManager;
import org.wgu.termtracker.models.TermModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class TermListActivity extends AppCompatActivity {
    private static final String TAG = "TermListActivity";

    @Inject
    TermManager termManager;

    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @BindView(R.id.termListView)
    ListView termList;

    ArrayAdapter<TermModel> termListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_term_list);
        ButterKnife.bind(this);

        setSupportActionBar(actionBar);

        List<TermModel> terms = termManager.listTerms();

        termListAdapter = new ArrayAdapter<TermModel>(this,android.R.layout.simple_list_item_1,
                terms);
        termList.setAdapter(termListAdapter);
        termList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TermListActivity.this.onTermClick(position, id);
            }
        });
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

        intent.putExtra(Constants.TYPE, Constants.ADD);

        startActivity(intent);
    }

    protected void onTermClick(int position, long id) {
        TermModel term = termListAdapter.getItem(position);

        Log.d(TAG, String.format("%s %s-%s", term.getTermId(), term.getStartDate(), term.getEndDate()));
    }
}
