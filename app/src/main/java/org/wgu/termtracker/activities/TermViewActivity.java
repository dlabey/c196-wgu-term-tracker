package org.wgu.termtracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.R;
import org.wgu.termtracker.data.CourseManager;
import org.wgu.termtracker.data.TermManager;
import org.wgu.termtracker.layout.NonScrollListView;
import org.wgu.termtracker.models.CourseModel;
import org.wgu.termtracker.models.TermModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class TermViewActivity extends AppCompatActivity {
    private static final String TAG = "TermViewActivity";

    @Inject
    TermManager termManager;

    @Inject
    CourseManager courseManager;

    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @BindView(R.id.titleTextView)
    TextView title;

    @BindView(R.id.startDateTextView)
    TextView startDate;

    @BindView(R.id.endDateTextView)
    TextView endDate;

    @BindView(R.id.courseListView)
    NonScrollListView courseList;

    @BindView(R.id.emptyCourseListTextView)
    TextView emptyCourseList;

    protected TermModel term;

    protected ArrayAdapter<CourseModel> courseListAdapter;

    protected List<CourseModel> courses;

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

        courses = courseManager.listCourses(term.getTermId());

        courseListAdapter = new ArrayAdapter<CourseModel>(this, android.R.layout.simple_list_item_1,
                courses);
        courseList.setAdapter(courseListAdapter);
        courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TermViewActivity.this.onCourseClick(position, id);
            }
        });
        courseList.setEmptyView(emptyCourseList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.term_view, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.home:
                intent = new Intent(this, HomeActivity.class);

                startActivity(intent);
                break;
            case R.id.editTerm:
                this.onEditClick(item.getActionView());
                break;
            case R.id.deleteTerm:
                this.onDeleteClick(item.getActionView());
                break;
            case R.id.listTerms:
                 intent = new Intent(this, TermListActivity.class);

                startActivity(intent);
                break;
            case R.id.addCourse:
                intent = new Intent(this, CourseInputActivity.class);

                intent.putExtra(Constants.TYPE, Constants.ADD);
                intent.putExtra(Constants.TERM, term);

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
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, Constants.OK,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (courses.size() > 0) {
                            Toast.makeText(getBaseContext(), "Error, courses still exist for this " +
                                            "term.", Toast.LENGTH_SHORT).show();
                        } else {
                            termManager.deleteTerm(term.getTermId());

                            Intent intent = new Intent(TermViewActivity.this, TermListActivity.class);

                            startActivity(intent);

                            finish();
                        }
                    }
                }
        );
        alertDialog.show();

        Log.d(TAG, String.format("onDeleteClick", term.getTermId()));
    }

    protected void onEditClick(View view) {
        Intent intent = new Intent(this, TermInputActivity.class);

        intent.putExtra(Constants.TYPE, Constants.EDIT);
        intent.putExtra(Constants.TERM, term);

        startActivity(intent);

        Log.d(TAG, String.format("onEditClick", term.getTermId()));
    }

    protected void onCourseClick(int position, long id) {
        CourseModel course = courseListAdapter.getItem(position);

        Intent intent = new Intent(this, CourseViewActivity.class);

        intent.putExtra(Constants.TERM, term);
        intent.putExtra(Constants.COURSE, course);

        startActivity(intent);

        Log.d(TAG, String.format("%s %s-%s", term.getTermId(), term.getStartDate(), term.getEndDate()));
    }
}
