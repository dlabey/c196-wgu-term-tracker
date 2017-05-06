package org.wgu.termtracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import org.wgu.termtracker.models.CourseMentorModel;

import java.util.LinkedList;
import java.util.List;

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

        values.put(CourseMentorEntry.COLUMN_NAME_COURSE_ID, courseId);
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

    public List<CourseMentorModel> listCourseMentors(long courseId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = { BaseColumns._ID, CourseMentorEntry.COLUMN_NAME_NAME,
            CourseMentorEntry.COLUMN_NAME_PHONE_NUMBER, CourseMentorEntry.COLUMN_NAME_EMAIL };

        String selection = String.format("%s = ?", CourseMentorEntry.COLUMN_NAME_COURSE_ID);
        String[] selectionArgs = { String.valueOf(courseId) };

        String sortOrder = "name";

        Cursor cursor = db.query(CourseMentorEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);

        List<CourseMentorModel> courseMentors = new LinkedList<>();

        while(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(
                    CourseMentorEntry.COLUMN_NAME_NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(
                    CourseMentorEntry.COLUMN_NAME_PHONE_NUMBER));
            String email = cursor.getString(cursor.getColumnIndex(
                    CourseMentorEntry.COLUMN_NAME_EMAIL));

            CourseMentorModel courseMentor = new CourseMentorModel();
            courseMentor.setName(name);
            courseMentor.setPhoneNumber(phoneNumber);
            courseMentor.setEmail(email);

            courseMentors.add(courseMentor);
        }
        cursor.close();

        Log.d(TAG, "Course Mentors");
        Log.d(TAG, String.valueOf(courseMentors.size()));

        return courseMentors;
    }
}
