package org.wgu.termtracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.provider.BaseColumns;
import android.util.Log;

import org.wgu.termtracker.Constants;
import org.wgu.termtracker.enums.CourseStatusEnum;
import org.wgu.termtracker.models.CourseModel;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class CourseManager extends DBManager implements CourseContract {
    private static final String TAG = "CourseManager";

    public CourseManager(Context context) {
        super(context);
    }

    public long createCourse(long termId, String title, Date startDate, Date anticipatedEndDate,
                             Date dueDate, CourseStatusEnum status) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        String startDateFormatted = simpleDateFormat.format(startDate);
        String anticipatedEndDateFormatted = simpleDateFormat.format(anticipatedEndDate);
        String dueDateFormatted = simpleDateFormat.format(dueDate);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CourseEntry.COLUMN_NAME_TERM_ID, termId);
        values.put(CourseEntry.COLUMN_NAME_TITLE, title);
        values.put(CourseEntry.COLUMN_NAME_START_DATE, startDateFormatted);
        values.put(CourseEntry.COLUMN_NAME_ANTICIPATED_END_DATE, anticipatedEndDateFormatted);
        values.put(CourseEntry.COLUMN_NAME_DUE_DATE, dueDateFormatted);
        values.put(CourseEntry.COLUMN_NAME_STATUS, status.getValue());

        return db.insert(CourseEntry.TABLE_NAME, null, values);
    }

    public boolean updateCourse(long courseId, String title, Date startDate,
                                Date anticipatedEndDate, Date dueDate, CourseStatusEnum status) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        String startDateFormatted = simpleDateFormat.format(startDate);
        String anticipatedEndDateFormatted = simpleDateFormat.format(anticipatedEndDate);
        String dueDateFormatted = simpleDateFormat.format(dueDate);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CourseEntry.COLUMN_NAME_TITLE, title);
        values.put(CourseEntry.COLUMN_NAME_START_DATE, startDateFormatted);
        values.put(CourseEntry.COLUMN_NAME_ANTICIPATED_END_DATE, anticipatedEndDateFormatted);
        values.put(CourseEntry.COLUMN_NAME_DUE_DATE, dueDateFormatted);
        values.put(CourseEntry.COLUMN_NAME_STATUS, status.getValue());

        String selection = String.format("%s = ?", BaseColumns._ID);
        String[] selectionArgs = { String.valueOf(courseId) };

        return db.update(CourseEntry.TABLE_NAME, values, selection, selectionArgs) > 0;
    }

    public boolean deleteCourse(long courseId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = String.format("%s = ?", BaseColumns._ID);
        String[] selectionArgs = { String.valueOf(courseId) };

        return db.delete(CourseEntry.TABLE_NAME, selection, selectionArgs) > 0;
    }

    public List<CourseModel> listCourses(long termId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = { BaseColumns._ID, CourseEntry.COLUMN_NAME_TITLE,
                CourseEntry.COLUMN_NAME_TITLE, CourseEntry.COLUMN_NAME_START_DATE,
                CourseEntry.COLUMN_NAME_ANTICIPATED_END_DATE, CourseEntry.COLUMN_NAME_DUE_DATE,
                CourseEntry.COLUMN_NAME_STATUS};

        String selection = String.format("%s = ?", CourseEntry.COLUMN_NAME_TERM_ID);
        String[] selectionArgs = { String.valueOf(termId) };

        String sortOrder = "DATE(start_date), DATE(due_date)";

        Cursor cursor = db.query(CourseEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                null, sortOrder);

        List<CourseModel> courses = new LinkedList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        while(cursor.moveToNext()) {
            long courseId = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_TITLE));
            String startDateStr = cursor
                    .getString(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_START_DATE));
            String anticipatedEndDateStr = cursor
                    .getString(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_ANTICIPATED_END_DATE));
            String dueDateStr = cursor
                    .getString(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_DUE_DATE));
            CourseStatusEnum status = CourseStatusEnum.valueOf(cursor.getInt(
                cursor.getColumnIndex(CourseEntry.COLUMN_NAME_STATUS)
            ));

            Date startDate = null;
            Date anticipatedEndDate = null;
            Date dueDate = null;
            try {
                startDate = simpleDateFormat.parse(startDateStr);
                anticipatedEndDate = simpleDateFormat.parse(anticipatedEndDateStr);
                dueDate = simpleDateFormat.parse(dueDateStr);
            } catch (ParseException ex) {
                Log.e(TAG, ex.getMessage());
            }

            CourseModel course = new CourseModel();
            course.setCourseId(courseId);
            course.setTitle(title);
            course.setStartDate(startDate);
            course.setAnticipatedEndDate(anticipatedEndDate);
            course.setDueDate(dueDate);
            course.setStatus(status);

            courses.add(course);
        }
        cursor.close();

        return courses;
    }
}
