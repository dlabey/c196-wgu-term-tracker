package org.wgu.termtracker.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.R;
import org.wgu.termtracker.data.CourseMentorManager;
import org.wgu.termtracker.models.CourseMentorModel;
import org.wgu.termtracker.models.CourseModel;
import org.wgu.termtracker.models.TermModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class CourseMentorInputActivity extends AppCompatActivity implements Validator.ValidationListener {
    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @Inject
    CourseMentorManager courseMentorManager;

    @BindView(R.id.nameEditText)
    @NotEmpty
    EditText name;

    @BindView(R.id.phoneNumberEditText)
    @NotEmpty
    EditText phoneNumber;

    @BindView(R.id.emailEditText)
    @NotEmpty
    @Email
    EditText email;

    protected String type;

    protected Validator validator;

    protected TermModel term;

    protected CourseModel course;

    protected CourseMentorModel courseMentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_mentor_input);
        ButterKnife.bind(this);

        setSupportActionBar(actionBar);

        type = getIntent().getStringExtra(Constants.TYPE);

        switch (type) {
            case Constants.ADD:
                getSupportActionBar().setTitle("Add Course Mentor");

                term = (TermModel) getIntent().getSerializableExtra(Constants.TERM);
                course = (CourseModel) getIntent().getSerializableExtra(Constants.COURSE);
                courseMentor = null;
                break;
            case Constants.EDIT:
                break;
        }

        validator = new Validator(this);
        validator.setValidationListener(this);

        if (courseMentor != null) {
            // TODO handle edit
        }
    }

    public void onSaveButtonClick(View view) {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        switch (type) {
            case Constants.ADD:
                long newCourseMentorId = courseMentorManager.createCourseMentor(
                        course.getCourseId(), name.getText().toString(),
                        phoneNumber.getText().toString(), email.getText().toString());

                saveAlert(newCourseMentorId > 0);
                break;
            case Constants.EDIT:
                break;
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();

            String message = error.getCollatedErrorMessage(this);

            ((EditText) view).setError(message);
        }
    }

    protected void saveAlert(final boolean result) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Notice");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (result) {
                            Intent intent = new Intent(CourseMentorInputActivity.this,
                                    CourseViewActivity.class);

                            intent.putExtra(Constants.TERM, term);
                            intent.putExtra(Constants.COURSE, course);

                            startActivity(intent);
                        }
                    }
                }
        );

        if (result) {
            alertDialog.setMessage("Course Mentor saved");
        } else {
            alertDialog.setMessage("Error, Course Mentor not saved");
        }

        alertDialog.show();
    }
}