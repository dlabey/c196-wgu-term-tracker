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

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.R;
import org.wgu.termtracker.data.CourseManager;
import org.wgu.termtracker.data.CourseMentorManager;
import org.wgu.termtracker.layout.NonScrollListView;
import org.wgu.termtracker.models.CourseMentorModel;
import org.wgu.termtracker.models.CourseModel;
import org.wgu.termtracker.models.TermModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class CourseViewActivity extends AppCompatActivity {
    private static final String TAG = "CourseViewActivity";

    @Inject
    CourseManager courseManager;

    @Inject
    CourseMentorManager courseMentorManager;

    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @BindView(R.id.titleTextView)
    TextView title;

    @BindView(R.id.startDateTextView)
    TextView startDate;

    @BindView(R.id.anticipatedEndDateTextView)
    TextView anticipatedEndDate;

    @BindView(R.id.dueDateTextView)
    TextView dueDate;

    @BindView(R.id.statusTextView)
    TextView status;

    @BindView(R.id.courseMentorListView)
    NonScrollListView courseMentorList;

    protected TermModel term;

    protected CourseModel course;

    protected ArrayAdapter<CourseMentorModel> courseMentorListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_view);
        ButterKnife.bind(this);

        setSupportActionBar(actionBar);

        term = (TermModel) getIntent().getSerializableExtra(Constants.TERM);
        course = (CourseModel) getIntent().getSerializableExtra(Constants.COURSE);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        String startDateStr = simpleDateFormat.format(course.getStartDate());
        String anticipatedEndDateStr = simpleDateFormat.format(course.getAnticipatedEndDate());
        String dueDateStr = simpleDateFormat.format(course.getDueDate());

        title.setText(course.getTitle());
        startDate.setText(startDateStr);
        anticipatedEndDate.setText(anticipatedEndDateStr);
        dueDate.setText(dueDateStr);
        status.setText(course.getStatus().toString());

        List<CourseMentorModel> courseMentors = courseMentorManager.listCourseMentors(
                course.getCourseId());

        courseMentorListAdapter = new ArrayAdapter<CourseMentorModel>(this,
                android.R.layout.simple_list_item_1, courseMentors);
        courseMentorList.setAdapter(courseMentorListAdapter);
        courseMentorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CourseViewActivity.this.onCourseMentorClick(position, id);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_view, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.editCourse:
                this.onEditClick(item.getActionView());
                break;
            case R.id.deleteCourse:
                this.onDeleteClick(item.getActionView());
                break;
            case R.id.courseTerm:
                intent = new Intent(this, TermViewActivity.class);

                intent.putExtra(Constants.TERM, term);

                startActivity(intent);
                break;
            case R.id.addCourseMentor:
                intent = new Intent(this, CourseMentorInputActivity.class);

                intent.putExtra(Constants.TYPE, Constants.ADD);
                intent.putExtra(Constants.TERM, term);
                intent.putExtra(Constants.COURSE, course);

                startActivity(intent);
                break;
            case R.id.addAssessment:
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
                        //TODO: Check if there are any assessment
                        courseManager.deleteCourse(course.getCourseId());

                        Intent intent = new Intent(CourseViewActivity.this, TermViewActivity.class);

                        intent.putExtra(Constants.TERM, term);

                        startActivity(intent);

                        finish();
                    }
                }
        );
        alertDialog.show();

        Log.d(TAG, String.format("onDeleteClick", term.getTermId()));
    }

    protected void onEditClick(View view) {
        Intent intent = new Intent(this, CourseInputActivity.class);

        intent.putExtra(Constants.TYPE, Constants.EDIT);
        intent.putExtra(Constants.TERM, term);
        intent.putExtra(Constants.COURSE, course);

        startActivity(intent);

        Log.d(TAG, String.format("onEditClick", term.getTermId()));
    }

    protected void onCourseMentorClick(int position, long id) {
        Log.d(TAG, String.format("%s %s-%s", term.getTermId(), term.getStartDate(), term.getEndDate()));
    }
}
