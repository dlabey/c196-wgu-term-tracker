package org.wgu.termtracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.commons.lang.StringUtils;

import java.util.Iterator;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WGUTermTracker.db";

    private static final String SQL_CREATE_ASSESSMENTS_TABLE =
            DatabaseContract.Assessment.showCreateTable();
    private static final String SQL_CREATE_ASSESSMENT_NOTES_TABLE =
            DatabaseContract.AssessmentNote.showCreateTable();
    private static final String SQL_CREATE_COURSES_TABLE =
            DatabaseContract.Course.showCreateTable();
    private static final String SQL_CREATE_COURSE_MENTORS_TABLE =
            DatabaseContract.CourseMentor.showCreateTable();
    private static final String SQL_CREATE_COURSE_NOTES_TABLE =
            DatabaseContract.CourseNote.showCreateTable();
    private static final String SQL_CREATE_NOTES_TABLE =
            DatabaseContract.Note.showCreateTable();
    private static final String SQL_CREATE_TERMS_TABLE =
            DatabaseContract.Term.showCreateTable();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ASSESSMENTS_TABLE);
        db.execSQL(SQL_CREATE_ASSESSMENT_NOTES_TABLE);
        db.execSQL(SQL_CREATE_COURSES_TABLE);
        db.execSQL(SQL_CREATE_COURSE_MENTORS_TABLE);
        db.execSQL(SQL_CREATE_COURSE_NOTES_TABLE);
        db.execSQL(SQL_CREATE_NOTES_TABLE);
        db.execSQL(SQL_CREATE_TERMS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing for now
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    static String generateCreateSql(String tableName, Map<String, String> columns) {
        String createTable = String.format("CREATE TABLE %s", tableName);

        String[] createColumnsArr = new String[columns.size()];
        Iterator it = columns.entrySet().iterator();
        int i = 0;

        while(it.hasNext()) {
            Map.Entry column = (Map.Entry) it.next();

            createColumnsArr[i] = String.format("%s %s", column.getKey(), column.getValue());

            i++;
        }

        String createColumns = StringUtils.join(createColumnsArr, ",%n");

        return String.format("%s,%n%s", createTable, createColumns);
    }
}
