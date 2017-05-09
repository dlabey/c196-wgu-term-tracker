package org.wgu.termtracker.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.AnnotationRule;
import com.mobsandgeeks.saripaar.QuickRule;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.mobsandgeeks.saripaar.rule.NotEmptyRule;

import org.apache.commons.lang.StringUtils;
import org.wgu.termtracker.Constants;
import org.wgu.termtracker.R;
import org.wgu.termtracker.data.NoteManager;
import org.wgu.termtracker.enums.AssessmentTypeEnum;
import org.wgu.termtracker.enums.NoteTypeEnum;
import org.wgu.termtracker.models.AssessmentModel;
import org.wgu.termtracker.models.CourseModel;
import org.wgu.termtracker.models.NoteModel;
import org.wgu.termtracker.models.TermModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class NoteInputActivity extends AppCompatActivity implements Validator.ValidationListener {
    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @Inject
    NoteManager noteManager;

    @BindView(R.id.typeSpinner)
    @Select
    Spinner noteType;

    @BindView(R.id.textEditText)
    EditText text;

    protected String photoUri;

    protected String actionType;

    protected String noteForType;

    protected Validator validator;

    protected TermModel term;

    protected CourseModel course;

    protected AssessmentModel assessment;

    protected NoteModel note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note_input);
        ButterKnife.bind(this);

        setSupportActionBar(actionBar);

        actionType = getIntent().getStringExtra(Constants.ACTION_TYPE);

        ArrayAdapter<NoteTypeEnum> noteTypeAdapter = new ArrayAdapter<NoteTypeEnum>(this,
                android.R.layout.simple_spinner_item, NoteTypeEnum.values());

        this.noteType.setAdapter(noteTypeAdapter);

        validator = new Validator(this);
        validator.setValidationListener(this);
        //noinspection unchecked
        // TODO: write validator for photo property
        validator.put(text, new QuickRule<EditText>() {
            NoteTypeEnum selectedNoteType = (NoteTypeEnum) noteType.getSelectedItem();

            @Override
            public boolean isValid(EditText view) {
                NoteTypeEnum selectedNoteType = (NoteTypeEnum) noteType.getSelectedItem();
                boolean valid = true;

                switch (selectedNoteType) {
                    case Text:
                        valid = !StringUtils.isEmpty(view.getText().toString());
                    break;
                }

                return valid;
            }

            @Override
            public String getMessage(Context context) {
                NoteTypeEnum selectedNoteType = (NoteTypeEnum) noteType.getSelectedItem();
                String message = StringUtils.EMPTY;

                switch (selectedNoteType) {
                    case Text:
                        message = "Text is required";
                    break;
                    case Photo:
                        message = "Photo is required";
                    break;
                }

                return message;
            }
        });

        if (note != null) {

        }
    }

    public void onSaveButtonClick(View view) {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        long newNoteId = -1;

        switch (actionType) {
            case Constants.ADD:
                switch (noteForType) {
                    case Constants.COURSE:
                        newNoteId = noteManager.createCourseNote(course.getCourseId(),
                            (NoteTypeEnum) noteType.getSelectedItem(), text.getText().toString(),
                            photoUri);

                        saveAlert(newNoteId > 0);
                    break;
                    case Constants.ASSESSMENT:
                        newNoteId = noteManager.createCourseNote(course.getCourseId(),
                            (NoteTypeEnum) noteType.getSelectedItem(), text.getText().toString(),
                            photoUri);

                        saveAlert(newNoteId > 0);
                    break;
                }
                break;
            case Constants.EDIT:
                boolean noteUpdated = noteManager.updateNote(note.getNoteId(),
                    (NoteTypeEnum) noteType.getSelectedItem(), text.getText().toString(),
                    photoUri);

                saveAlert(noteUpdated);
                break;
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

                        Intent intent;

                        if (result) {
                            switch (actionType) {
                                case Constants.COURSE:
                                    intent = new Intent(NoteInputActivity.this,
                                            CourseViewActivity.class);

                                    intent.putExtra(Constants.TERM, term);
                                    intent.putExtra(Constants.COURSE, course);

                                    startActivity(intent);
                                    break;
                                case Constants.ASSESSMENT:
                                    intent = new Intent(NoteInputActivity.this,
                                            AssessmentViewActivity.class);

                                    intent.putExtra(Constants.TERM, term);
                                    intent.putExtra(Constants.COURSE, course);
                                    intent.putExtra(Constants.ASSESSMENT, assessment);

                                    startActivity(intent);
                                    break;
                            }
                        }
                    }
                }
        );

        if (result) {
            alertDialog.setMessage("Note saved");
        } else {
            alertDialog.setMessage("Error, Note not saved");
        }

        alertDialog.show();
    }
}
