package org.wgu.termtracker.data;

import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.inject.Singleton;

@Singleton
interface DBContract {

    static class Assessment implements BaseColumns {
        static final String TABLE_NAME = "assessments";
        static final String COLUMN_NAME_TITLE = "title";
        static final String COLUMN_NAME_DUE_DATE = "due_date";
        static final String COLUMN_NAME_TYPE = "type";
        static final String COLUMN_NAME_COURSE_ID = "course_id";

        static String showCreateTable() {
            HashMap<String, String> columns = new LinkedHashMap<>();

            columns.put(BaseColumns._ID, "INTEGER PRIMARY KEY");
            columns.put(COLUMN_NAME_TITLE, "TEXT");
            columns.put(COLUMN_NAME_DUE_DATE, "TEXT");
            columns.put(COLUMN_NAME_TYPE, "INTEGER");
            columns.put(COLUMN_NAME_COURSE_ID, "INTEGER");

            return DBManager.generateCreateSql(TABLE_NAME, columns);
        }
    }

    static class AssessmentNote implements BaseColumns {
        static final String TABLE_NAME = "assessment_notes";
        static final String COLUMN_NAME_ASSESSMENT_ID = "assessment_id";
        static final String COLUMN_NAME_NOTE_ID = "note_id";

        static String showCreateTable() {
            HashMap<String, String> columns = new LinkedHashMap<>();

            columns.put(BaseColumns._ID, "INTEGER PRIMARY KEY");
            columns.put(COLUMN_NAME_ASSESSMENT_ID, "INTEGER");
            columns.put(COLUMN_NAME_NOTE_ID, "INTEGER");

            return DBManager.generateCreateSql(TABLE_NAME, columns);
        }
    }

    static class Course implements BaseColumns {
        static final String TABLE_NAME = "courses";
        static final String COLUMN_NAME_TITLE = "title";
        static final String COLUMN_NAME_START_DATE = "start_date";
        static final String COLUMN_NAME_ANTICIPATED_END_DATE =  "anticipated_end_date";
        static final String COLUMN_NAME_DUE_DATE = "due_date";
        static final String COLUMN_NAME_STATUS = "status";
        static final String COLUMN_NAME_TERM_ID = "term_id";

        static String showCreateTable() {
            HashMap<String, String> columns = new LinkedHashMap<>();

            columns.put(BaseColumns._ID, "INTEGER PRIMARY KEY");
            columns.put(COLUMN_NAME_TITLE, "TEXT");
            columns.put(COLUMN_NAME_START_DATE, "TEXT");
            columns.put(COLUMN_NAME_ANTICIPATED_END_DATE, "TEXT");
            columns.put(COLUMN_NAME_DUE_DATE, "TEXT");
            columns.put(COLUMN_NAME_STATUS, "INTEGER");
            columns.put(COLUMN_NAME_TERM_ID, "INTEGER");

            return DBManager.generateCreateSql(TABLE_NAME, columns);
        }
    }

    static class CourseMentor implements BaseColumns {
        static final String TABLE_NAME = "course_mentors";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";
        static final String COLUMN_NAME_EMAIL = "email";
        static final String COLUMN_NAME_COURSE_ID = "course_id";

        static String showCreateTable() {
            HashMap<String, String> columns = new LinkedHashMap<>();

            columns.put(BaseColumns._ID, "INTEGER PRIMARY KEY");
            columns.put(COLUMN_NAME_NAME, "TEXT");
            columns.put(COLUMN_NAME_PHONE_NUMBER, "TEXT");
            columns.put(COLUMN_NAME_EMAIL, "TEXT");
            columns.put(COLUMN_NAME_COURSE_ID, "INTEGER");

            return DBManager.generateCreateSql(TABLE_NAME, columns);
        }
    }

    static class CourseNote implements BaseColumns {
        static final String TABLE_NAME = "course_notes";
        static final String COLUMN_NAME_COURSE_ID = "course_id";
        static final String COLUMN_NAME_NOTE_ID = "note_id";

        static String showCreateTable() {
            HashMap<String, String> columns = new LinkedHashMap<>();

            columns.put(BaseColumns._ID, "INTEGER PRIMARY KEY");
            columns.put(COLUMN_NAME_COURSE_ID, "INTEGER");
            columns.put(COLUMN_NAME_NOTE_ID, "INTEGER");

            return DBManager.generateCreateSql(TABLE_NAME, columns);
        }
    }

    static class Note implements BaseColumns {
        static final String TABLE_NAME = "notes";
        static final String COLUMN_NAME_TYPE = "type";
        static final String COLUMN_NAME_TEXT = "text";
        static final String COLUMN_NAME_FILENAME = "filename";

        static String showCreateTable() {
            HashMap<String, String> columns = new LinkedHashMap<>();

            columns.put(BaseColumns._ID, "INTEGER PRIMARY KEY");
            columns.put(COLUMN_NAME_TYPE, "INTEGER");
            columns.put(COLUMN_NAME_TEXT, "TEXT");
            columns.put(COLUMN_NAME_FILENAME, "TEXT");

            return DBManager.generateCreateSql(TABLE_NAME, columns);
        }
    }

    static class Term implements BaseColumns {
        static final String TABLE_NAME = "notes";
        static final String COLUMN_NAME_TITLE = "type";
        static final String COLUMN_NAME_START_DATE = "start_date";
        static final String COLUMN_NAME_END_DATE = "end_date";

        static String showCreateTable() {
            HashMap<String, String> columns = new LinkedHashMap<>();

            columns.put(BaseColumns._ID, "INTEGER PRIMARY KEY");
            columns.put(COLUMN_NAME_TITLE, "TEXT");
            columns.put(COLUMN_NAME_START_DATE, "TEXT");
            columns.put(COLUMN_NAME_END_DATE, "TEXT");

            return DBManager.generateCreateSql(TABLE_NAME, columns);
        }
    }
}
