package org.wgu.termtracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.R;
import org.wgu.termtracker.data.CourseManager;
import org.wgu.termtracker.data.CourseMentorManager;
import org.wgu.termtracker.models.CourseMentorModel;
import org.wgu.termtracker.models.CourseModel;
import org.wgu.termtracker.models.TermModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class CourseMentorViewActivity extends AppCompatActivity {
    private static final String TAG = "CourseMentorViewAct";

    @Inject
    CourseMentorManager courseMentorManager;

    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @BindView(R.id.nameTextView)
    TextView name;

    @BindView(R.id.phoneNumberTextView)
    TextView phoneNumber;

    @BindView(R.id.emailTextView)
    TextView email;

    protected TermModel term;

    protected CourseModel course;

    protected CourseModel courseModel;

    protected CourseMentorModel courseMentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_mentor_view);
        ButterKnife.bind(this);

        setSupportActionBar(actionBar);

        term = (TermModel) getIntent().getSerializableExtra(Constants.TERM);
        course = (CourseModel) getIntent().getSerializableExtra(Constants.COURSE);
        courseMentor = (CourseMentorModel) getIntent().getSerializableExtra(Constants.COURSE_MENTOR);

        name.setText(courseMentor.getName());
        phoneNumber.setText(courseMentor.getPhoneNumber());
        email.setText(courseMentor.getEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_mentor_view, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.editCourseMentor:
                this.onEditClick(item.getActionView());
                break;
            case R.id.deleteCourseMentor:
                this.onDeleteClick(item.getActionView());
                break;
            case R.id.course:
                intent = new Intent(this, CourseViewActivity.class);

                intent.putExtra(Constants.TERM, term);
                intent.putExtra(Constants.COURSE, course);

                startActivity(intent);
                break;
        }

        return false;
    }

    protected void onDeleteClick(View view) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();

        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Are you sure you want to delete this Course Mentor?");
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
                        courseMentorManager.deleteCourseMentor(courseMentor.getCourseMentorId());

                        Intent intent = new Intent(CourseMentorViewActivity.this,
                                CourseViewActivity.class);

                        intent.putExtra(Constants.TERM, term);
                        intent.putExtra(Constants.COURSE, course);

                        startActivity(intent);

                        finish();
                    }
                }
        );
        alertDialog.show();

        Log.d(TAG, String.format("onDeleteClick", term.getTermId()));
    }

    protected void onEditClick(View view) {
        Intent intent = new Intent(this, CourseMentorInputActivity.class);

        intent.putExtra(Constants.TYPE, Constants.EDIT);
        intent.putExtra(Constants.TERM, term);
        intent.putExtra(Constants.COURSE, course);
        intent.putExtra(Constants.COURSE_MENTOR, courseMentor);

        startActivity(intent);

        Log.d(TAG, String.format("onEditClick", term.getTermId()));
    }
}
