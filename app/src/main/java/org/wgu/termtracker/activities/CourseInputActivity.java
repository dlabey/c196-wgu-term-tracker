package org.wgu.termtracker.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Spinner;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class CourseInputActivity extends AppCompatActivity implements Validator.ValidationListener {
    private static final String TAG = "CourseInputActivity";

    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @BindView(R.id.titleEditText)
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
    @NotEmpty
    Spinner status;

    protected String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_input);
        ButterKnife.bind(this);

        type = getIntent().getStringExtra(Constants.TYPE);
    }

    @Override
    public void onValidationSucceeded() {

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

    }
}
