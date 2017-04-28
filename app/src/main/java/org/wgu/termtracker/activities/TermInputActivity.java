package org.wgu.termtracker.activities;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.R;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class TermInputActivity extends AppCompatActivity implements ValidationListener {

    private static final String TAG = "TermInputActivity";

    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @BindView(R.id.titleEditText)
    @NotEmpty
    EditText title;

    @BindView(R.id.startDateEditText)
    @NotEmpty
    @Pattern(regex = Constants.DATE_REGEX)
    EditText startDate;

    @BindView(R.id.endDateEditText)
    @NotEmpty
    @Pattern(regex = Constants.DATE_REGEX)
    EditText endDate;

    String type;

    DatePickerDialog datePickerDialog;

    EditText dateDialogEditText;

    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_term_input);
        ButterKnife.bind(this);

        setSupportActionBar(actionBar);

        type = getIntent().getStringExtra(Constants.TYPE);

        switch (type) {
            case Constants.ADD:
                getSupportActionBar().setTitle("Add Termz");
                break;
            case Constants.EDIT:
                getSupportActionBar().setTitle("Edit Term");
                break;
        }

        startDate.setKeyListener(null);
        endDate.setKeyListener(null);

        Calendar calendar = Calendar.getInstance();

        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(TermInputActivity.this,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateDialogEditText.setText(String.format("%02d/%02d/%s", month + 1, day, year));
            }
        }, currentYear, currentMonth, currentDay);

        startDate.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    TermInputActivity.this.onDateClick(view);
                } else {
                    datePickerDialog.hide();
                }
            }
        });
        endDate.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    TermInputActivity.this.onDateClick(view);
                } else {
                    datePickerDialog.hide();
                }
            }
        });

        validator = new Validator(this);
        validator.setValidationListener(this);
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date startDateFormatted = simpleDateFormat.parse(startDate.getText().toString());
            Date endDateFormatted = simpleDateFormat.parse(endDate.getText().toString());

            if (startDateFormatted.after(endDateFormatted)) {
                startDate.setError("Start Date cannot be after End Date");
            } else {
                startDate.setError(null);
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

            ((EditText) view).setError(message);
        }
    }
}
