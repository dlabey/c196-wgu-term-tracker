package org.wgu.termtracker.database;

import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class DatabaseContract {
    private DatabaseContract() {}

    public static class Assessment implements BaseColumns {
        public static final String TABLE_NAME = "assessments";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DUE_DATE = "due_date";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_COURSE_ID = "course_id";

        public static final String showCreateTable() {
            HashMap<String, String> columns = new LinkedHashMap<>();

            columns.put(BaseColumns._ID, "INTEGER PRIMARY KEY");
            columns.put(COLUMN_NAME_TITLE, "TEXT");
            columns.put(COLUMN_NAME_DUE_DATE, "TEXT");
            columns.put(COLUMN_NAME_TYPE, "INTEGER");
            columns.put(COLUMN_NAME_COURSE_ID, "INTEGER");

            return DatabaseHelper.generateCreateSql(TABLE_NAME, columns);
        }
    }

    public static class AssessmentNote implements BaseColumns {
        public static final String TABLE_NAME = "assessment_notes";
        public static final String COLUMN_NAME_ASSESSMENT_ID = "assessment_id";
        public static final String COLUMN_NAME_NOTE_ID = "note_id";

        public static final String showCreateTable() {
            HashMap<String, String> columns = new LinkedHashMap<>();

            columns.put(BaseColumns._ID, "INTEGER PRIMARY KEY");
            columns.put(COLUMN_NAME_ASSESSMENT_ID, "INTEGER");
            columns.put(COLUMN_NAME_NOTE_ID, "INTEGER");

            return DatabaseHelper.generateCreateSql(TABLE_NAME, columns);
        }
    }

    public static class Course implements BaseColumns {
        public static final String TABLE_NAME = "courses";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_START_DATE = "start_date";
        public static final String COLUMN_NAME_ANTICIPATED_END_DATE =  "anticipated_end_date";
        public static final String COLUMN_NAME_DUE_DATE = "due_date";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_TERM_ID = "term_id";

        public static final String showCreateTable() {
            HashMap<String, String> columns = new LinkedHashMap<>();

            columns.put(BaseColumns._ID, "INTEGER PRIMARY KEY");
            columns.put(COLUMN_NAME_TITLE, "TEXT");
            columns.put(COLUMN_NAME_START_DATE, "TEXT");
            columns.put(COLUMN_NAME_ANTICIPATED_END_DATE, "TEXT");
            columns.put(COLUMN_NAME_DUE_DATE, "TEXT");
            columns.put(COLUMN_NAME_STATUS, "INTEGER");
            columns.put(COLUMN_NAME_TERM_ID, "INTEGER");

            return DatabaseHelper.generateCreateSql(TABLE_NAME, columns);
        }
    }

    public static class CourseMentor implements BaseColumns {
        public static final String TABLE_NAME = "course_mentors";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_COURSE_ID = "course_id";

        public static final String showCreateTable() {
            HashMap<String, String> columns = new LinkedHashMap<>();

            columns.put(BaseColumns._ID, "INTEGER PRIMARY KEY");
            columns.put(COLUMN_NAME_NAME, "TEXT");
            columns.put(COLUMN_NAME_PHONE_NUMBER, "TEXT");
            columns.put(COLUMN_NAME_EMAIL, "TEXT");
            columns.put(COLUMN_NAME_COURSE_ID, "INTEGER");

            return DatabaseHelper.generateCreateSql(TABLE_NAME, columns);
        }
    }

    public static class CourseNote implements BaseColumns {
        public static final String TABLE_NAME = "course_notes";
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
        public static final String COLUMN_NAME_NOTE_ID = "note_id";

        public static final String showCreateTable() {
            HashMap<String, String> columns = new LinkedHashMap<>();

            columns.put(BaseColumns._ID, "INTEGER PRIMARY KEY");
            columns.put(COLUMN_NAME_COURSE_ID, "INTEGER");
            columns.put(COLUMN_NAME_NOTE_ID, "INTEGER");

            return DatabaseHelper.generateCreateSql(TABLE_NAME, columns);
        }
    }

    public static class Note implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_FILENAME = "filename";

        public static final String showCreateTable() {
            HashMap<String, String> columns = new LinkedHashMap<>();

            columns.put(BaseColumns._ID, "INTEGER PRIMARY KEY");
            columns.put(COLUMN_NAME_TYPE, "INTEGER");
            columns.put(COLUMN_NAME_TEXT, "TEXT");
            columns.put(COLUMN_NAME_FILENAME, "TEXT");

            return DatabaseHelper.generateCreateSql(TABLE_NAME, columns);
        }
    }

    public static class Term implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_TITLE = "type";
        public static final String COLUMN_NAME_START_DATE = "start_date";
        public static final String COLUMN_NAME_END_DATE = "end_date";

        public static final String showCreateTable() {
            HashMap<String, String> columns = new LinkedHashMap<>();

            columns.put(BaseColumns._ID, "INTEGER PRIMARY KEY");
            columns.put(COLUMN_NAME_TITLE, "TEXT");
            columns.put(COLUMN_NAME_START_DATE, "TEXT");
            columns.put(COLUMN_NAME_END_DATE, "TEXT");

            return DatabaseHelper.generateCreateSql(TABLE_NAME, columns);
        }
    }
}
