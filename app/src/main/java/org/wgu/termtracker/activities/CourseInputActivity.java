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
import org.wgu.termtracker.data.CourseManager;
import org.wgu.termtracker.enums.CourseStatusEnum;
import org.wgu.termtracker.models.CourseModel;
import org.wgu.termtracker.models.TermModel;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class CourseInputActivity extends AppCompatActivity implements Validator.ValidationListener {
    private static final String TAG = "CourseInputActivity";

    @Inject
    CourseManager courseManager;

    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @BindView(R.id.titleEditText)
    @NotEmpty
    EditText title;

    @BindView(R.id.startDateEditText)
    @NotEmpty
    @Pattern(regex = Constants.DATE_REGEX)
    EditText startDate;

    @BindView(R.id.anticipatedEndDateEditText)
    @NotEmpty
    @Pattern(regex = Constants.DATE_REGEX)
    EditText anticipatedEndDate;

    @BindView(R.id.dueDateEditText)
    @NotEmpty
    @Pattern(regex = Constants.DATE_REGEX)
    EditText dueDate;

    @BindView(R.id.statusSpinner)
    @Select
    Spinner status;

    protected String type;

    protected DatePickerDialog datePickerDialog;

    protected EditText dateDialogEditText;

    protected Map<Integer, String> statuses;

    protected Validator validator;

    protected TermModel term;

    protected CourseModel course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_input);
        ButterKnife.bind(this);

        setSupportActionBar(actionBar);

        type = getIntent().getStringExtra(Constants.TYPE);

        switch (type) {
            case Constants.ADD:
                getSupportActionBar().setTitle("Add Course");

                term = (TermModel) getIntent().getSerializableExtra(Constants.TERM);
                course = null;
                break;
            case Constants.EDIT:
                getSupportActionBar().setTitle("Edit Course");

                term = (TermModel) getIntent().getSerializableExtra(Constants.TERM);
                course = (CourseModel) getIntent().getSerializableExtra(Constants.COURSE);
                break;
        }

        startDate.setKeyListener(null);
        anticipatedEndDate.setKeyListener(null);
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

        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    CourseInputActivity.this.onDateClick(view);
                } else {
                    datePickerDialog.hide();
                }
            }
        });
        anticipatedEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    CourseInputActivity.this.onDateClick(view);
                } else {
                    datePickerDialog.hide();
                }
            }
        });
        dueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    CourseInputActivity.this.onDateClick(view);
                } else {
                    datePickerDialog.hide();
                }
            }
        });

        ArrayAdapter<CourseStatusEnum> statusAdapter = new ArrayAdapter<CourseStatusEnum>(this,
                android.R.layout.simple_spinner_item, CourseStatusEnum.values());

        this.status.setAdapter(statusAdapter);

        validator = new Validator(this);
        validator.setValidationListener(this);

        if (course != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

            String startDateStr = simpleDateFormat.format(course.getStartDate());
            String anticipatedEndDateStr = simpleDateFormat.format(course.getAnticipatedEndDate());
            String dueDateStr = simpleDateFormat.format(course.getDueDate());

            title.setText(course.getTitle());
            startDate.setText(startDateStr);
            anticipatedEndDate.setText(anticipatedEndDateStr);
            dueDate.setText(dueDateStr);
            status.setSelection(course.getStatus().getValue());
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
            Date startDateParsed = simpleDateFormat.parse(startDate.getText().toString());
            Date anticipatedEndDateParsed = simpleDateFormat.parse(anticipatedEndDate.getText()
                    .toString());
            Date dueDateParsed = simpleDateFormat.parse(dueDate.getText().toString());

            if (startDateParsed.after(anticipatedEndDateParsed)
                    || startDateParsed.after(dueDateParsed)) {
                startDate.setError("Start Date cannot be after Anticipated End Date or Due Date");
            } else {
                startDate.setError(null);

                switch (type) {
                    case Constants.ADD:
                        long newCourseId = courseManager.createCourse(term.getTermId(),
                                title.getText().toString(), startDateParsed,
                                anticipatedEndDateParsed, dueDateParsed,
                                (CourseStatusEnum) status.getSelectedItem());

                        saveAlert(newCourseId > 0);
                        break;
                    case Constants.EDIT:
                        boolean courseUpdated = courseManager.updateCourse(course.getCourseId(),
                                title.getText().toString(), startDateParsed,
                                anticipatedEndDateParsed, dueDateParsed,
                                (CourseStatusEnum) status.getSelectedItem());

                        saveAlert(courseUpdated);
                        break;
                }
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
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (result) {
                            Intent intent = new Intent(CourseInputActivity.this,
                                    CourseViewActivity.class);

                            intent.putExtra(Constants.TERM, term);
                            intent.putExtra(Constants.COURSE, course);

                            startActivity(intent);
                        }
                    }
                }
        );

        if (result) {
            alertDialog.setMessage("Course saved");
        } else {
            alertDialog.setMessage("Error, Course not saved");
        }

        alertDialog.show();
    }
}
