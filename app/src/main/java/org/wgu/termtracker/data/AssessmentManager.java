package org.wgu.termtracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.provider.BaseColumns;
import android.util.Log;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.enums.AssessmentTypeEnum;
import org.wgu.termtracker.models.AssessmentModel;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class AssessmentManager extends DBManager implements AsessmentContract {
    private static final String TAG = "AsessmentManager";

    public AssessmentManager(Context context) {
        super(context);
    }

    public long createAssessment(long courseId, String title, Date dueDate,
                                 AssessmentTypeEnum type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        String dueDateFormatted = simpleDateFormat.format(dueDate);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(AssessmentEntry.COLUMN_NAME_COURSE_ID, courseId);
        values.put(AssessmentEntry.COLUMN_NAME_TITLE, title);
        values.put(AssessmentEntry.COLUMN_NAME_DUE_DATE, dueDateFormatted);
        values.put(AssessmentEntry.COLUMN_NAME_TYPE, type.getValue());

        return db.insert(AssessmentEntry.TABLE_NAME, null, values);
    }

    public boolean updateAssessment(long assessmentId, String title, Date dueDate,
                                    AssessmentTypeEnum type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        String dueDateFormatted = simpleDateFormat.format(dueDate);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(AssessmentEntry.COLUMN_NAME_TITLE, title);
        values.put(AssessmentEntry.COLUMN_NAME_DUE_DATE, dueDateFormatted);
        values.put(AssessmentEntry.COLUMN_NAME_TYPE, type.getValue());

        String selection = String.format("%s = ?", BaseColumns._ID);
        String[] selectionArgs = { String.valueOf(assessmentId) };

        return db.update(AssessmentEntry.TABLE_NAME, values, selection, selectionArgs) > 0;
    }

    public boolean deleteAssessment(long assessmentId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = String.format("%s = ?", BaseColumns._ID);
        String[] selectionArgs = { String.valueOf(assessmentId) };

        return db.delete(AssessmentEntry.TABLE_NAME, selection, selectionArgs) > 0;
    }

    public List<AssessmentModel> listAssessments(long courseId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = { BaseColumns._ID, AssessmentEntry.COLUMN_NAME_TITLE,
                AssessmentEntry.COLUMN_NAME_TITLE, AssessmentEntry.COLUMN_NAME_DUE_DATE,
                AssessmentEntry.COLUMN_NAME_TYPE};

        String selection = String.format("%s = ?", AssessmentEntry.COLUMN_NAME_COURSE_ID);
        String[] selectionArgs = { String.valueOf(courseId) };

        String sortOrder = "DATE(due_date)";

        Cursor cursor = db.query(AssessmentEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);

        List<AssessmentModel> assessments = new LinkedList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        while(cursor.moveToNext()) {
            long assessmentId = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            String title = cursor.getString(cursor
                    .getColumnIndex(AssessmentEntry.COLUMN_NAME_TITLE));
            String dueDateStr = cursor
                    .getString(cursor.getColumnIndex(AssessmentEntry.COLUMN_NAME_DUE_DATE));
            AssessmentTypeEnum type = AssessmentTypeEnum.valueOf(cursor.getInt(
                cursor.getColumnIndex(AssessmentEntry.COLUMN_NAME_TYPE)
            ));

            Date dueDate = null;
            try {
                dueDate = simpleDateFormat.parse(dueDateStr);
            } catch (ParseException ex) {
                Log.e(TAG, ex.getMessage());
            }

            AssessmentModel assessment = new AssessmentModel();
            assessment.setAssessmentId(assessmentId);
            assessment.setTitle(title);
            assessment.setDueDate(dueDate);
            assessment.setType(type);

            assessments.add(assessment);
        }
        cursor.close();

        return assessments;
    }
}
