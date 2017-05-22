package org.wgu.termtracker.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import org.wgu.termtracker.data.PreferencesManager;
import org.wgu.termtracker.enums.CourseStatusEnum;
import org.wgu.termtracker.models.CourseModel;
import org.wgu.termtracker.models.PreferencesModel;
import org.wgu.termtracker.models.TermModel;
import org.wgu.termtracker.notifications.NotificationScheduler;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class CourseInputActivity extends AppCompatActivity implements Validator.ValidationListener {
    private static final String TAG = "CourseInputActivity";

    protected static String COURSE_NOTIFICATION_TITLE = "Course Notification";

    protected static String COURSE_START_NOTIFICATION_CONTENT = "Course is starting in %s days " +
            "for course %s";

    protected static String COURSE_END_NOTIFICATION_CONTENT = "Course is ending in %s days " +
            "for course %s";

    @Inject
    CourseManager courseManager;

    @Inject
    PreferencesManager preferencesManager;

    @Inject
    NotificationScheduler notificationScheduler;

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

    @BindView(R.id.alertNotesTextView)
    TextView alertNotes;

    protected PreferencesModel preferences;

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

        preferences = preferencesManager.getPreferences();

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

        if (preferences.isCourseAlerts()) {
            alertNotes.setText("Alerts are currently on for course notifications. You will get " +
                    "a notification based on the days before defined in preferences for the " +
                    "start and due date of the course.");
        } else {
            alertNotes.setText("Alerts are currently off for course notifications. You can " +
                    "change this in preferences.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

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
            case R.id.preferences:
                intent = new Intent(this, PreferencesActivity.class);

                startActivity(intent);
                break;
        }

        return false;
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

        boolean actionSuccessful = false;

         long notificationDelay = TimeUnit.MILLISECONDS.convert(preferences.getCourseAlertDays(),
                TimeUnit.DAYS);

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

                        actionSuccessful = newCourseId > 0;

                        saveAlert(actionSuccessful);

                        // create course model for notification use if successfully created
                        if (actionSuccessful) {
                            course = new CourseModel();
                            course.setCourseId(newCourseId);
                            course.setTitle(title.getText().toString());
                            course.setStartDate(startDateParsed);
                            course.setAnticipatedEndDate(anticipatedEndDateParsed);
                            course.setDueDate(dueDateParsed);
                            course.setStatus((CourseStatusEnum) status.getSelectedItem());
                        }
                        break;
                    case Constants.EDIT:
                        actionSuccessful = courseManager.updateCourse(course.getCourseId(),
                                title.getText().toString(), startDateParsed,
                                anticipatedEndDateParsed, dueDateParsed,
                                (CourseStatusEnum) status.getSelectedItem());

                        saveAlert(actionSuccessful);

                        // update due date if action successful for immediate use
                        if (actionSuccessful) {
                            course.setDueDate(dueDateParsed);
                        }
                        break;
                }
            }
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
        }

        if (preferences.isCourseAlerts() && actionSuccessful) {
            // start notification alert
            String content = String.format(COURSE_START_NOTIFICATION_CONTENT,
                    preferences.getCourseAlertDays(), title.getText().toString());

            // start delay
            long startDelay = course.getStartDate().getTime() - notificationDelay;

            // use the 10000s for start
            notificationScheduler.scheduleNotification(CourseViewActivity.class, startDelay,
                    (int) (10000 + course.getCourseId()), term, course, COURSE_NOTIFICATION_TITLE,
                    content);

            // end notification alert
            content = String.format(COURSE_END_NOTIFICATION_CONTENT,
                    preferences.getCourseAlertDays(), course.getTitle());

            // end delay
            long untilDueDate = course.getDueDate().getTime() - notificationDelay -
                    System.currentTimeMillis();
            long dueDelay = SystemClock.currentThreadTimeMillis() + untilDueDate;

            // use the 20000s for end
            notificationScheduler.scheduleNotification(CourseViewActivity.class, dueDelay,
                    (int) (20000 + course.getCourseId()), term, course, COURSE_NOTIFICATION_TITLE,
                    content);
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
                            Intent intent = new Intent(CourseInputActivity.this,
                                    TermViewActivity.class);

                            intent.putExtra(Constants.TERM, term);

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
