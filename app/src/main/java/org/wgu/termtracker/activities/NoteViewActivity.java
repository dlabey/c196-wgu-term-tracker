package org.wgu.termtracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.R;
import org.wgu.termtracker.data.NoteManager;
import org.wgu.termtracker.models.AssessmentModel;
import org.wgu.termtracker.models.CourseModel;
import org.wgu.termtracker.models.NoteModel;
import org.wgu.termtracker.models.TermModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class NoteViewActivity extends AppCompatActivity {
    private static final String TAG = "NoteViewActivity";

    @Inject
    NoteManager noteManager;

    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @BindView(R.id.textTextView)
    TextView text;

    @BindView(R.id.photoImageView)
    ImageView photo;

    protected String noteForType;

    protected TermModel term;

    protected CourseModel course;

    protected AssessmentModel assessment;

    protected NoteModel note;

    protected boolean isPhotoFitToScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note_view);
        ButterKnife.bind(this);

        setSupportActionBar(actionBar);

        noteForType = getIntent().getStringExtra(Constants.NOTE_FOR_TYPE);

        term = (TermModel) getIntent().getSerializableExtra(Constants.TERM);
        course = (CourseModel) getIntent().getSerializableExtra(Constants.COURSE);
        assessment = (AssessmentModel) getIntent().getSerializableExtra(Constants.ASSESSMENT);
        note = (NoteModel) getIntent().getSerializableExtra(Constants.NOTE);

        text.setText(note.getText());

        String photoUriStr = note.getPhotoUri();

        if (photoUriStr != null) {
            Uri photoUri = Uri.parse(photoUriStr);

            photo.setImageURI(photoUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_view, menu);

        MenuItem objectMenuItem = menu.findItem(R.id.object);

        switch (noteForType) {
            case Constants.COURSE:
                objectMenuItem.setTitle("Course");
                break;
            case Constants.ASSESSMENT:
                objectMenuItem.setTitle("Assessment");
                break;
            default:
                objectMenuItem.setVisible(false);
                break;
        }

        return true;
    }

    public void onPhotoClick(View view) {
        if (isPhotoFitToScreen) {
            isPhotoFitToScreen = false;

            photo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            photo.setAdjustViewBounds(true);
        } else {
            isPhotoFitToScreen = true;

            photo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            photo.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.shareNote:
                break;
            case R.id.editNote:
                this.onEditClick(item.getActionView());
                break;
            case R.id.deleteNote:
                this.onDeleteClick(item.getActionView());
                break;
            case R.id.object:
                switch (noteForType) {
                    case Constants.COURSE:
                        intent = new Intent(this, CourseViewActivity.class);
                        intent.putExtra(Constants.TERM, term);
                        intent.putExtra(Constants.COURSE, course);
                        break;
                    case Constants.ASSESSMENT:
                        intent = new Intent(this, AssessmentViewActivity.class);
                        intent.putExtra(Constants.TERM, term);
                        intent.putExtra(Constants.COURSE, course);
                        intent.putExtra(Constants.ASSESSMENT, assessment);
                        break;
                    default:
                        intent = new Intent(this, TermViewActivity.class);
                        break;
                }

                startActivity(intent);
                break;
        }

        return false;
    }

    protected void onDeleteClick(View view) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();

        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Are you sure you want to delete this note?");
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
                        Intent intent;

                        Log.d(TAG, String.valueOf(note.getNoteId()));

                        switch (noteForType) {
                            case Constants.COURSE:
                                noteManager.deleteCourseNote(note.getNoteId());

                                intent = new Intent(NoteViewActivity.this, CourseViewActivity.class);
                                intent.putExtra(Constants.TERM, term);
                                intent.putExtra(Constants.COURSE, course);
                                break;
                            case Constants.ASSESSMENT:
                                noteManager.deleteAssessmentNote(note.getNoteId());

                                intent = new Intent(NoteViewActivity.this, AssessmentViewActivity.class);
                                intent.putExtra(Constants.TERM, term);
                                intent.putExtra(Constants.COURSE, course);
                                intent.putExtra(Constants.ASSESSMENT, assessment);
                                break;
                            default:
                                intent = new Intent(NoteViewActivity.this, TermListActivity.class);
                                break;
                        }

                        startActivity(intent);

                        finish();
                    }
                }
        );
        alertDialog.show();

        Log.d(TAG, String.format("onDeleteClick", note.getNoteId()));
    }

    protected void onEditClick(View view) {
        Intent intent = new Intent(this, NoteInputActivity.class);

        switch (noteForType) {
            case Constants.ASSESSMENT:
                intent.putExtra(Constants.ASSESSMENT, assessment);
            case Constants.COURSE:
                intent.putExtra(Constants.ACTION_TYPE, Constants.EDIT);
                intent.putExtra(Constants.NOTE_FOR_TYPE, noteForType);
                intent.putExtra(Constants.TERM, term);
                intent.putExtra(Constants.COURSE, course);
                intent.putExtra(Constants.NOTE, note);
                break;
            default:
                intent = new Intent(NoteViewActivity.this, TermListActivity.class);
                break;
        }

        startActivity(intent);

        Log.d(TAG, String.format("onEditClick", note.getNoteId()));
    }
}
