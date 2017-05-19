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
import android.widget.Toast;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mobsandgeeks.saripaar.annotation.Select;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.R;
import org.wgu.termtracker.data.AssessmentManager;
import org.wgu.termtracker.data.NoteManager;
import org.wgu.termtracker.layout.NonScrollListView;
import org.wgu.termtracker.models.AssessmentModel;
import org.wgu.termtracker.models.CourseModel;
import org.wgu.termtracker.models.NoteModel;
import org.wgu.termtracker.models.TermModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class AssessmentViewActivity extends AppCompatActivity {
    private static final String TAG = "AssessmentViewActivity";

    @Inject
    AssessmentManager assessmentManager;

    @Inject
    NoteManager noteManager;

    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @BindView(R.id.titleTextView)
    TextView title;

    @BindView(R.id.dueDateTextView)
    @NotEmpty
    @Pattern(regex = Constants.DATE_REGEX)
    TextView dueDate;

    @BindView(R.id.typeTextView)
    @Select
    TextView assessmentType;

    @BindView(R.id.assessmentNoteListView)
    NonScrollListView assessmentNoteList;

    @BindView(R.id.emptyAssessmentNoteListTextView)
    TextView emptyAssessmentNoteList;

    protected TermModel term;

    protected CourseModel course;

    protected AssessmentModel assessment;

    protected ArrayAdapter<NoteModel> assessmentNoteListAdapter;

    protected List<NoteModel> assessmentNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_assessment_view);
        ButterKnife.bind(this);

        setSupportActionBar(actionBar);

        term = (TermModel) getIntent().getSerializableExtra(Constants.TERM);
        course = (CourseModel) getIntent().getSerializableExtra(Constants.COURSE);
        assessment = (AssessmentModel) getIntent().getSerializableExtra(Constants.ASSESSMENT);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        String dueDateStr = simpleDateFormat.format(course.getDueDate());

        title.setText(assessment.getTitle());
        dueDate.setText(dueDateStr);
        assessmentType.setText(assessment.getType().toString());

        assessmentNotes = noteManager.listAssessmentNotes(assessment.getAssessmentId());

        assessmentNoteListAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, assessmentNotes);
        assessmentNoteList.setAdapter(assessmentNoteListAdapter);
        assessmentNoteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AssessmentViewActivity.this.onAssessmentNoteClick(position, id);
            }
        });
        assessmentNoteList.setEmptyView(emptyAssessmentNoteList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.assessment_view, menu);

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
            case R.id.editAssessment:
                this.onEditClick(item.getActionView());
                break;
            case R.id.deleteAssessment:
                this.onDeleteClick(item.getActionView());
                break;
            case R.id.course:
                intent = new Intent(this, CourseViewActivity.class);

                intent.putExtra(Constants.TERM, term);
                intent.putExtra(Constants.COURSE, course);

                startActivity(intent);
                break;
            case R.id.addNote:
                intent = new Intent(this, NoteInputActivity.class);

                intent.putExtra(Constants.ACTION_TYPE, Constants.ADD);
                intent.putExtra(Constants.NOTE_FOR_TYPE, Constants.ASSESSMENT);
                intent.putExtra(Constants.TERM, term);
                intent.putExtra(Constants.COURSE, course);
                intent.putExtra(Constants.ASSESSMENT, assessment);

                startActivity(intent);
                break;
        }

        return false;
    }

    public void onDeleteClick(View view) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();

        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Are you sure you want to delete this Assessment?");
        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, Constants.OK,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (assessmentNotes.size() > 0) {
                            Toast.makeText(getBaseContext(), "Error, assessment notes still exist " +
                                            "for this assessment.", Toast.LENGTH_SHORT).show();
                        } else {
                            assessmentManager.deleteAssessment(assessment.getAssessmentId());

                            Intent intent = new Intent(AssessmentViewActivity.this,
                                    CourseViewActivity.class);

                            intent.putExtra(Constants.TERM, term);
                            intent.putExtra(Constants.COURSE, course);

                            startActivity(intent);

                            finish();
                        }
                    }
                }
        );
        alertDialog.show();

        Log.d(TAG, String.format("onDeleteClick", term.getTermId()));
    }

    public void onEditClick(View view) {
        Intent intent = new Intent(this, AssessmentInputActivity.class);

        intent.putExtra(Constants.TYPE, Constants.EDIT);
        intent.putExtra(Constants.TERM, term);
        intent.putExtra(Constants.COURSE, course);
        intent.putExtra(Constants.ASSESSMENT, assessment);

        startActivity(intent);

        Log.d(TAG, String.format("onEditClick", assessment.getAssessmentId()));
    }

    public void onAssessmentNoteClick(int position, long id) {
        NoteModel note = assessmentNoteListAdapter.getItem(position);

        Intent intent = new Intent(this, NoteViewActivity.class);

        intent.putExtra(Constants.NOTE_FOR_TYPE, Constants.ASSESSMENT);
        intent.putExtra(Constants.TERM, term);
        intent.putExtra(Constants.COURSE, course);
        intent.putExtra(Constants.ASSESSMENT, assessment);
        intent.putExtra(Constants.NOTE, note);

        startActivity(intent);

        Log.d(TAG, String.format("%s", note.toString()));
    }
}
