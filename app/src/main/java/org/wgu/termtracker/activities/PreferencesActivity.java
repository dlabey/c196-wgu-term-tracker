package org.wgu.termtracker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.ToggleButton;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.wgu.termtracker.App;
import org.wgu.termtracker.R;
import org.wgu.termtracker.data.PreferencesManager;
import org.wgu.termtracker.enums.SharingMethodEnum;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class PreferencesActivity extends AppCompatActivity {

    private static final String TAG = "PreferencesActivity";

    @Inject
    App app;

    @Inject
    PreferencesManager prefsManager;

    @BindView(R.id.courseAlerts)
    Switch courseAlerts;

    @BindView(R.id.courseAlertMinutes)
    DiscreteSeekBar courseAlertMinutes;

    @BindView(R.id.assessmentAlerts)
    Switch assessmentAlerts;

    @BindView(R.id.assessmentAlertMinutes)
    DiscreteSeekBar assessmentAlertMinutes;

    @BindView(R.id.sharingMethod)
    ToggleButton sharingMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preferences);
        ButterKnife.bind(this);
    }

    public void onSaveButtonClick(View view) {
        boolean isCourseAlerts = courseAlerts.isChecked();
        int courseAlertMinutes = 10;
        boolean isAssessmentAlerts = assessmentAlerts.isChecked();
        int assessmentAlertMinutes = 10;
        SharingMethodEnum sharingMethod = SharingMethodEnum.EMAIL;

        boolean result = this.prefsManager.setPreferences(isCourseAlerts, courseAlertMinutes,
                isAssessmentAlerts, assessmentAlertMinutes, sharingMethod);

        Log.d(TAG, prefsManager.toString());
        Log.d(TAG, String.valueOf(result));
    }
}
