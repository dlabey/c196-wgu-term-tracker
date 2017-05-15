package org.wgu.termtracker.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mobsandgeeks.saripaar.annotation.Select;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.R;
import org.wgu.termtracker.data.AssessmentManager;
import org.wgu.termtracker.enums.AssessmentTypeEnum;
import org.wgu.termtracker.enums.CourseStatusEnum;
import org.wgu.termtracker.models.AssessmentModel;
import org.wgu.termtracker.models.CourseMentorModel;
import org.wgu.termtracker.models.CourseModel;
import org.wgu.termtracker.models.TermModel;
import org.wgu.termtracker.notifications.NotificationScheduler;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class AssessmentInputActivity extends AppCompatActivity
        implements Validator.ValidationListener {
    private static final String TAG = "AssessmentInputActivity";

    @Inject
    AssessmentManager assessmentManager;

    @Inject
    NotificationScheduler notificationScheduler;

    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @BindView(R.id.titleEditText)
    @NotEmpty
    EditText title;

    @BindView(R.id.dueDateEditText)
    @NotEmpty
    @Pattern(regex = Constants.DATE_REGEX)
    EditText dueDate;

    @BindView(R.id.typeSpinner)
    @Select
    Spinner assessmentType;

    protected String type;

    protected DatePickerDialog datePickerDialog;

    protected EditText dateDialogEditText;

    protected Validator validator;

    protected TermModel term;

    protected CourseModel course;

    protected AssessmentModel assessment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_assessment_input);
        ButterKnife.bind(this);

        setSupportActionBar(actionBar);

        type = getIntent().getStringExtra(Constants.TYPE);

        switch (type) {
            case Constants.ADD:
                getSupportActionBar().setTitle("Add Assessment");

                term = (TermModel) getIntent().getSerializableExtra(Constants.TERM);
                course = (CourseModel) getIntent().getSerializableExtra(Constants.COURSE);
                assessment = null;
                break;
            case Constants.EDIT:
                getSupportActionBar().setTitle("Edit Assessment");

                term = (TermModel) getIntent().getSerializableExtra(Constants.TERM);
                course = (CourseModel) getIntent().getSerializableExtra(Constants.COURSE);
                assessment = (AssessmentModel) getIntent()
                        .getSerializableExtra(Constants.ASSESSMENT);
                break;
        }

        dueDate.setKeyListener(null);

        Calendar calendar = Calendar.getInstance();

        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        dateDialogEditText.setText(String.format("%02d/%02d/%s", month + 1, day, year));
                    }
                }, currentYear, currentMonth, currentDay);

        dueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    AssessmentInputActivity.this.onDateClick(view);
                } else {
                    datePickerDialog.hide();
                }
            }
        });

        ArrayAdapter<AssessmentTypeEnum> statusAdapter = new ArrayAdapter<AssessmentTypeEnum>(this,
                android.R.layout.simple_spinner_item, AssessmentTypeEnum.values());

        this.assessmentType.setAdapter(statusAdapter);

        validator = new Validator(this);
        validator.setValidationListener(this);

        if (assessment != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

            String dueDateStr = simpleDateFormat.format(course.getDueDate());

            title.setText(assessment.getTitle());
            dueDate.setText(dueDateStr);
            assessmentType.setSelection(assessment.getType().getValue());
        }
    }

    public void onDateClick(View view) {
        dateDialogEditText = (EditText) view;

        datePickerDialog.show();
    }

    public void onSaveButtonClick(View view) {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        try {
            Date dueDateParsed = simpleDateFormat.parse(dueDate.getText().toString());

            switch (type) {
                case Constants.ADD:
                    long newAssessmentId = assessmentManager.createAssessment(course.getCourseId(),
                            title.getText().toString(), dueDateParsed,
                            (AssessmentTypeEnum) assessmentType.getSelectedItem());

                    saveAlert(newAssessmentId > 0);
                    break;
                case Constants.EDIT:
                    boolean assessmentUpdated = assessmentManager.updateAssessment(
                            assessment.getAssessmentId(), title.getText().toString(), dueDateParsed,
                            (AssessmentTypeEnum) assessmentType.getSelectedItem());

                    saveAlert(assessmentUpdated);
                    break;
            }
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();

            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else if (view instanceof Spinner) {
                ((TextView) ((Spinner) view).getSelectedView()).setError(message);
            }
        }
    }

    protected void saveAlert(final boolean result) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Notice");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, Constants.OK,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (result) {
                            Intent intent = new Intent(AssessmentInputActivity.this,
                                    CourseViewActivity.class);

                            intent.putExtra(Constants.TERM, term);
                            intent.putExtra(Constants.COURSE, course);

                            startActivity(intent);
                        }
                    }
                }
        );

        if (result) {
            alertDialog.setMessage("Assessment saved");

            notificationScheduler.scheduleNotification();
        } else {
            alertDialog.setMessage("Error, Assessment not saved");
        }

        alertDialog.show();
    }
}
