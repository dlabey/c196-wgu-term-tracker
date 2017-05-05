package org.wgu.termtracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import javax.inject.Singleton;

@Singleton
public class CourseMentorManager extends DBManager implements CourseMentorContract  {
    private static final String TAG = "CourseMentorManager";

    public CourseMentorManager(Context context) {
        super(context);
    }

    public long createCourseMentor(long courseId, String name, String phoneNumber, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CourseMentorEntry.COLUMN_NAME_NAME, name);
        values.put(CourseMentorEntry.COLUMN_NAME_PHONE_NUMBER, phoneNumber);
        values.put(CourseMentorEntry.COLUMN_NAME_EMAIL, email);


        return db.insert(CourseMentorEntry.TABLE_NAME, null, values);
    }

    public boolean updateCourseMentor(long courseMentorId, String name, String phoneNumber,
                                      String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CourseMentorEntry.COLUMN_NAME_NAME, name);
        values.put(CourseMentorEntry.COLUMN_NAME_PHONE_NUMBER, phoneNumber);
        values.put(CourseMentorEntry.COLUMN_NAME_EMAIL, email);

        String selection = String.format("%s = ?", BaseColumns._ID);
        String[] selectionArgs = { String.valueOf(courseMentorId) };

        return db.update(CourseMentorEntry.TABLE_NAME, values, selection, selectionArgs) > 0;
    }

    public boolean deleteCourseMentor(long courseMentorId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = String.format("%s = ?", BaseColumns._ID);
        String[] selectionArgs = { String.valueOf(courseMentorId) };

        return db.delete(CourseMentorEntry.TABLE_NAME, selection, selectionArgs) > 0;
    }
}
