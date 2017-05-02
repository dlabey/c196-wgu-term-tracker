package org.wgu.termtracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.R;
import org.wgu.termtracker.data.TermManager;
import org.wgu.termtracker.models.TermModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class TermViewActivity extends AppCompatActivity {
    private static final String TAG = "TermViewActivity";

    protected TermModel term;

    @Inject
    TermManager termManager;

    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @BindView(R.id.titleTextView)
    TextView title;

    @BindView(R.id.startDateTextView)
    TextView startDate;

    @BindView(R.id.endDateTextView)
    TextView endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_term_view);
        ButterKnife.bind(this);

        setSupportActionBar(actionBar);

        term = (TermModel) getIntent().getSerializableExtra(Constants.TERM);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        String startDateStr = simpleDateFormat.format(term.getStartDate());
        String endDateStr = simpleDateFormat.format(term.getEndDate());

        title.setText(term.getTitle());
        startDate.setText(startDateStr);
        endDate.setText(endDateStr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.term_view, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editTerm:
                this.onEditClick(item.getActionView());
                break;
            case R.id.deleteTerm:
                this.onDeleteClick(item.getActionView());
                break;
            case R.id.listTerms:
                Intent intent = new Intent(this, TermListActivity.class);

                startActivity(intent);
                break;
        }

        return false;
    }

    protected void onDeleteClick(View view) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();

        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Are you sure you want to delete this term?");
        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Check if there are any courses
                        termManager.deleteTerm(term.getTermId());

                        Intent intent = new Intent(TermViewActivity.this, TermListActivity.class);

                        startActivity(intent);
                    }
                }
        );
        alertDialog.show();

        Log.d(TAG, String.format("onDeleteClick", term.getTermId()));
    }

    protected void onEditClick(View view) {
        Log.d(TAG, String.format("onEditClick", term.getTermId()));
    }
}
