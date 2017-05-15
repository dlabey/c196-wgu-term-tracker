package org.wgu.termtracker.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.ToggleButton;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.wgu.termtracker.App;
import org.wgu.termtracker.Constants;
import org.wgu.termtracker.R;
import org.wgu.termtracker.data.PreferencesManager;
import org.wgu.termtracker.enums.SharingMethodEnum;
import org.wgu.termtracker.models.PreferencesModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class PreferencesActivity extends AppCompatActivity {

    private static final String TAG = "PreferencesActivity";
    @Inject
    PreferencesManager prefsManager;

    @BindView(R.id.courseAlertsSwitch)
    Switch courseAlerts;

    @BindView(R.id.courseAlertMinutesDiscreteSeekBar)
    DiscreteSeekBar courseAlertMinutes;

    @BindView(R.id.assessmentAlertsSwitch)
    Switch assessmentAlerts;

    @BindView(R.id.assessmentAlertMinutesDiscreteSeekBar)
    DiscreteSeekBar assessmentAlertMinutes;

    @BindView(R.id.sharingMethodToggleButton)
    ToggleButton sharingMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preferences);
        ButterKnife.bind(this);

        PreferencesModel prefs = this.prefsManager.getPreferences();

        courseAlerts.setChecked(prefs.isCourseAlerts());
        courseAlertMinutes.setProgress(prefs.getCourseAlertMinutes());
        assessmentAlerts.setChecked(prefs.isAssessmentAlerts());
        assessmentAlertMinutes.setProgress(prefs.getAssessmentAlertMinutes());
        sharingMethod.setChecked(prefs.getSharingMethod() == SharingMethodEnum.SMS);
    }

    public void onSaveButtonClick(View view) {
        boolean isCourseAlerts = this.courseAlerts.isChecked();
        int courseAlertMinutes = this.courseAlertMinutes.getProgress();
        boolean isAssessmentAlerts = assessmentAlerts.isChecked();
        int assessmentAlertMinutes = this.assessmentAlertMinutes.getProgress();
        SharingMethodEnum sharingMethod = SharingMethodEnum.valueOf(this.sharingMethod.getText()
                .toString().toUpperCase());

        boolean result = this.prefsManager.setPreferences(isCourseAlerts, courseAlertMinutes,
                isAssessmentAlerts, assessmentAlertMinutes, sharingMethod);

        saveAlert(result);

        Log.d(TAG, prefsManager.toString());
        Log.d(TAG, String.valueOf(result));
        Log.d(TAG, String.valueOf(prefsManager.getPreferences().getSharingMethod().getValue()));
    }

    protected void saveAlert(boolean result) {
        AlertDialog alertDialog = new AlertDialog.Builder(PreferencesActivity.this).create();

        alertDialog.setTitle("Notice");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, Constants.OK,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }
        );

        if (result) {
            alertDialog.setMessage("Preferences saved");
        } else {
            alertDialog.setMessage("Error, preferences not saved");
        }

        alertDialog.show();
    }
}
