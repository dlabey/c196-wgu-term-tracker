package org.wgu.termtracker.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.apache.commons.lang.StringUtils;
import org.wgu.termtracker.App;
import org.wgu.termtracker.Constants;
import org.wgu.termtracker.R;
import org.wgu.termtracker.data.NoteManager;
import org.wgu.termtracker.models.AssessmentModel;
import org.wgu.termtracker.models.CourseModel;
import org.wgu.termtracker.models.NoteModel;
import org.wgu.termtracker.models.TermModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class NoteInputActivity extends AppCompatActivity implements Validator.ValidationListener {
    private static final String TAG = "NoteInputActivity";

    private static final int REQUEST_CODE_PERMISSION = 766;

    private static final int SELECT_PHOTO = 9010;

    @Inject
    App app;

    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @Inject
    NoteManager noteManager;

    @BindView(R.id.textEditText)
    @NotEmpty
    EditText text;

    @BindView(R.id.photoImageView)
    ImageView photoChooser;

    @BindView(R.id.photoEditText)
    EditText photoText;

    protected Uri photoUri;

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
        noteForType = getIntent().getStringExtra(Constants.NOTE_FOR_TYPE);

        switch (actionType) {
            case Constants.ADD:
                getSupportActionBar().setTitle("Add Note");

                term = (TermModel) getIntent().getSerializableExtra(Constants.TERM);
                course = (CourseModel) getIntent().getSerializableExtra(Constants.COURSE);
                assessment = (AssessmentModel) getIntent().getSerializableExtra(Constants.ASSESSMENT);
                note = null;
                break;
            case Constants.EDIT:
                getSupportActionBar().setTitle("Edit Note");

                term = (TermModel) getIntent().getSerializableExtra(Constants.TERM);
                course = (CourseModel) getIntent().getSerializableExtra(Constants.COURSE);
                assessment = (AssessmentModel) getIntent().getSerializableExtra(Constants.ASSESSMENT);
                note = (NoteModel) getIntent().getSerializableExtra(Constants.NOTE);
                break;
        }

        photoText.setKeyListener(null);

        validator = new Validator(this);
        validator.setValidationListener(this);

        checkWritingReadingPermission();

        if (note != null) {
            String photoUriStr = note.getPhotoUri();

            text.setText(note.getText());

            if (photoUriStr != null) {
                photoText.setText(photoUriStr);
            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                photoChooser.setEnabled(true);
            } else {
                photoChooser.setEnabled(false);

                improperPermissionsAlert();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        Log.d(TAG, String.valueOf(requestCode));
        Log.d(TAG, String.valueOf(resultCode));

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PHOTO) {
                final boolean isCamera;

                if (data == null || data.getData() == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();

                    if (action == null) {
                        isCamera = false;

                        photoUri = data.getData();
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri = photoUri;

                photoText.setText(selectedImageUri.toString());

                Log.d(TAG, selectedImageUri.toString());
            }
        }
    }

    public void onPhotoButtonClick(View view) {
        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WGU");

        boolean mkdirs = root.mkdirs();

        String state = Environment.getExternalStorageState();

        String fileName = String.format("%s.jpg", System.currentTimeMillis());

        File sdImageMainDirectory = new File(root, fileName);

        photoUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() +
                ".provider", sdImageMainDirectory);

        Log.d(TAG, photoUri.toString());

        List<Intent> cameraIntents = new ArrayList<Intent>();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        PackageManager packageManager = getPackageManager();

        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);

        for(ResolveInfo resolvedInfo : listCam) {
            String packageName = resolvedInfo.activityInfo.packageName;

            Intent intent = new Intent(captureIntent);

            intent.setComponent(new ComponentName(packageName, resolvedInfo.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            cameraIntents.add(intent);
        }

        Intent galleryIntent = new Intent();

        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        Intent chooserIntent = Intent.createChooser(galleryIntent, "Select source");

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, SELECT_PHOTO);

        Log.d(TAG, String.valueOf(SELECT_PHOTO));
    }

    public void onSaveButtonClick(View view) {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        String photoUriStr = photoUri == null ? null : photoUri.toString();

        long newNoteId = -1;

        switch (actionType) {
            case Constants.ADD:
                switch (noteForType) {
                    case Constants.COURSE:
                        newNoteId = noteManager.createCourseNote(course.getCourseId(),
                            text.getText().toString(), photoUriStr);

                        saveAlert(newNoteId > 0);
                    break;
                    case Constants.ASSESSMENT:
                        newNoteId = noteManager.createAssessmentNote(assessment.getAssessmentId(),
                            text.getText().toString(), photoUriStr);

                        saveAlert(newNoteId > 0);
                    break;
                }
                break;
            case Constants.EDIT:
                if (StringUtils.isEmpty(photoUriStr) && !StringUtils.isEmpty(note.getPhotoUri())) {
                    photoUriStr = note.getPhotoUri();
                }

                boolean noteUpdated = noteManager.updateNote(note.getNoteId(),
                    text.getText().toString(), photoUriStr);

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

    protected void checkWritingReadingPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                improperPermissionsAlert();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            }
        }
    }

    protected void improperPermissionsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Notice");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, Constants.OK,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent intent;

                        switch (noteForType) {
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
        );

        alertDialog.setMessage("To make notes you need to allow permission to write and read from external storage");
        alertDialog.show();
    }

    protected void saveAlert(final boolean result) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Notice");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, Constants.OK,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent intent;

                        Log.d(TAG, String.valueOf(term));
                        Log.d(TAG, String.valueOf(course));

                        if (result) {
                            switch (noteForType) {
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
