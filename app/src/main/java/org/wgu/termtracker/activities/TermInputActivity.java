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
import android.view.Menu;
import android.view.MenuItem;
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
import org.wgu.termtracker.data.TermManager;
import org.wgu.termtracker.models.TermModel;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class TermInputActivity extends AppCompatActivity implements ValidationListener {
    private static final String TAG = "TermInputActivity";

    @Inject
    TermManager termManager;

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

    protected String type;

    protected DatePickerDialog datePickerDialog;

    protected EditText dateDialogEditText;

    protected Validator validator;

    protected TermModel term;

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
                getSupportActionBar().setTitle("Add Term");

                term = null;
                break;
            case Constants.EDIT:
                getSupportActionBar().setTitle("Edit Term");

                term = (TermModel) getIntent().getSerializableExtra(Constants.TERM);
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

        if (term != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

            String startDateStr = simpleDateFormat.format(term.getStartDate());
            String endDateStr = simpleDateFormat.format(term.getEndDate());

            title.setText(term.getTitle());
            startDate.setText(startDateStr);
            endDate.setText(endDateStr);
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

        try {
            Date startDateParsed = simpleDateFormat.parse(startDate.getText().toString());
            Date endDateParsed = simpleDateFormat.parse(endDate.getText().toString());

            if (startDateParsed.after(endDateParsed)) {
                startDate.setError("Start Date cannot be after End Date");
            } else {
                startDate.setError(null);

                switch (type) {
                    case Constants.ADD:
                        long newTermId = termManager.createTerm(title.getText().toString(),
                                startDateParsed, endDateParsed);

                        saveAlert(newTermId > 0);
                        break;
                    case Constants.EDIT:
                        boolean termUpdated = termManager.updateTerm(term.getTermId(),
                                title.getText().toString(), startDateParsed, endDateParsed);

                        saveAlert(termUpdated);
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

            ((EditText) view).setError(message);
        }
    }

    protected void saveAlert(final boolean result) {
        AlertDialog alertDialog = new AlertDialog.Builder(TermInputActivity.this).create();
        alertDialog.setTitle("Notice");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, Constants.OK,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (result) {
                            Intent intent = new Intent(TermInputActivity.this, TermListActivity.class);

                            startActivity(intent);
                        }
                    }
                }
        );

        if (result) {
            alertDialog.setMessage("Term saved");
        } else {
            alertDialog.setMessage("Error, Term not saved");
        }

        alertDialog.show();
    }
}
