package org.wgu.termtracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import org.apache.commons.lang.StringUtils;
import org.wgu.termtracker.enums.CourseStatusEnum;
import org.wgu.termtracker.enums.NoteTypeEnum;
import org.wgu.termtracker.models.CourseMentorModel;
import org.wgu.termtracker.models.NoteModel;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class NoteManager extends DBManager implements NoteContract {
    private static final String TAG = "NoteManager";

    public NoteManager(Context context) {
        super(context);
    }

    public long createCourseNote(long courseId, NoteTypeEnum type, String text, String photoUri) {
        long courseNoteId = -1;

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();

            ContentValues values = new ContentValues();

            values.put(NoteEntry.COLUMN_NAME_TYPE, type.getValue());
            values.put(NoteEntry.COLUMN_NAME_TEXT, text);
            values.put(NoteEntry.COLUMN_NAME_PHOTO_URI, photoUri);

            long newNoteId = db.insertOrThrow(NoteEntry.TABLE_NAME, null, values);

            values = new ContentValues();

            values.put(CourseNoteEntry.COLUMN_NAME_COURSE_ID, courseId);
            values.put(CourseNoteEntry.COLUMN_NAME_NOTE_ID, newNoteId);

            courseNoteId = db.insertOrThrow(CourseNoteEntry.TABLE_NAME, null, values);

            db.setTransactionSuccessful();
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            db.endTransaction();
        }

        return courseNoteId;
    }

    public long createAssessmentNote(long assessmentId, NoteTypeEnum type, String text,
                                     String photoUri) {
        long assessmentNodeId = -1;

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();

            ContentValues values = new ContentValues();

            values.put(NoteEntry.COLUMN_NAME_TYPE, type.getValue());
            values.put(NoteEntry.COLUMN_NAME_TEXT, text);
            values.put(NoteEntry.COLUMN_NAME_PHOTO_URI, photoUri);

            long newNoteId = db.insertOrThrow(NoteEntry.TABLE_NAME, null, values);

            values = new ContentValues();

            values.put(AssessmentNoteEntry.COLUMN_NAME_ASSESSMENT_ID, assessmentId);
            values.put(AssessmentNoteEntry.COLUMN_NAME_NOTE_ID, newNoteId);

            assessmentNodeId = db.insertOrThrow(AssessmentNoteEntry.TABLE_NAME, null, values);

            db.setTransactionSuccessful();
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            db.endTransaction();
        }

        return assessmentNodeId;
    }

    public boolean updateNote(long noteId, NoteTypeEnum type, String text, String photoUri) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NoteEntry.COLUMN_NAME_TYPE, type.getValue());
        values.put(NoteEntry.COLUMN_NAME_TEXT, text);
        values.put(NoteEntry.COLUMN_NAME_PHOTO_URI, photoUri);

        String selection = String.format("%s = ?", BaseColumns._ID);
        String[] selectionArgs = { String.valueOf(noteId) };

        return db.update(NoteEntry.TABLE_NAME, values, selection, selectionArgs) > 0;
    }

    public boolean deleteCourseNote(long noteId) {
        boolean deleted = false;

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();

            String selection = String.format("%s = ?", BaseColumns._ID);
            String[] selectionArgs = { String.valueOf(noteId) };

            deleted = db.delete(NoteEntry.TABLE_NAME, selection, selectionArgs) > 0;

            if (!deleted) throw new SQLException("No Note was deleted");

            selection = String.format("%s = ?", CourseNoteEntry.COLUMN_NAME_NOTE_ID);

            deleted = db.delete(CourseNoteEntry.TABLE_NAME, selection, selectionArgs) > 0;

            if (!deleted) throw new SQLException("No Course Note was deleted");

            db.setTransactionSuccessful();
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            db.endTransaction();
        }

        return deleted;
    }

    public boolean deleteAssessmentNote(long noteId) {
        boolean deleted = false;

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();

            String selection = String.format("%s = ?", BaseColumns._ID);
            String[] selectionArgs = { String.valueOf(noteId) };

            deleted = db.delete(NoteEntry.TABLE_NAME, selection, selectionArgs) > 0;

            if (!deleted) throw new SQLException("No Note was deleted");

            selection = String.format("%s = ?", AssessmentNoteEntry.COLUMN_NAME_NOTE_ID);

            deleted = db.delete(AssessmentNoteEntry.TABLE_NAME, selection, selectionArgs) > 0;

            if (!deleted) throw new SQLException("No Assessment Note was deleted");

            db.setTransactionSuccessful();
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            db.endTransaction();
        }

        return deleted;
    }

    public List<NoteModel> listCourseNotes(long courseId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String noteIdCol = String.format("%s.%s", NoteEntry.TABLE_NAME, BaseColumns._ID);

        String[] projection = { noteIdCol, NoteEntry.COLUMN_NAME_TYPE,
                NoteEntry.COLUMN_NAME_TEXT, NoteEntry.COLUMN_NAME_PHOTO_URI };

        String selection = String.format("%s = ? AND %s.%s = %s.%s",
                CourseNoteEntry.COLUMN_NAME_COURSE_ID,
                CourseNoteEntry.TABLE_NAME, CourseNoteEntry.COLUMN_NAME_NOTE_ID,
                NoteEntry.TABLE_NAME, BaseColumns._ID);
        String[] selectionArgs = { String.valueOf(courseId) };

        String sortOrder = noteIdCol;

        String tables = String.format("%s, %s", CourseNoteEntry.TABLE_NAME, NoteEntry.TABLE_NAME);

        Cursor cursor = db.query(tables, projection, selection, selectionArgs,
                null, null, sortOrder);

        List<NoteModel> courseNotes = new LinkedList<>();

        while(cursor.moveToNext()) {
            long noteId = cursor.getLong(0);
            NoteTypeEnum type = NoteTypeEnum.valueOf(cursor.getInt(
                    cursor.getColumnIndex(NoteEntry.COLUMN_NAME_TYPE)
            ));
            String text = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_TEXT));
            String photoUri = cursor.getString(cursor.getColumnIndex(
                    NoteEntry.COLUMN_NAME_PHOTO_URI));

            NoteModel courseNote = new NoteModel();
            courseNote.setNoteId(noteId);
            courseNote.setType(type);
            courseNote.setText(text);
            courseNote.setPhotoUri(photoUri);

            courseNotes.add(courseNote);
        }
        cursor.close();

        Log.d(TAG, "Course Notes");
        Log.d(TAG, String.valueOf(courseNotes.size()));

        return courseNotes;
    }

    public List<NoteModel> listAssessmentNotes(long assessmentId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String noteIdCol = String.format("%s.%s", NoteEntry.TABLE_NAME, BaseColumns._ID);

        String[] projection = { noteIdCol, NoteEntry.COLUMN_NAME_TYPE,
                NoteEntry.COLUMN_NAME_TEXT, NoteEntry.COLUMN_NAME_PHOTO_URI };

        String selection = String.format("%s = ? AND %s.%s = %s.%s",
                AssessmentNoteEntry.COLUMN_NAME_ASSESSMENT_ID,
                AssessmentNoteEntry.TABLE_NAME, AssessmentNoteEntry.COLUMN_NAME_NOTE_ID,
                NoteEntry.TABLE_NAME, BaseColumns._ID);
        String[] selectionArgs = { String.valueOf(assessmentId) };

        String sortOrder = noteIdCol;

        String tables = String.format("%s, %s", AssessmentNoteEntry.TABLE_NAME, NoteEntry.TABLE_NAME);

        Cursor cursor = db.query(tables, projection, selection, selectionArgs,
                null, null, sortOrder);

        List<NoteModel> assessmentNotes = new LinkedList<>();

        while(cursor.moveToNext()) {
            long noteId = cursor.getLong(0);
            NoteTypeEnum type = NoteTypeEnum.valueOf(cursor.getInt(
                    cursor.getColumnIndex(NoteEntry.COLUMN_NAME_TYPE)
            ));
            String text = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_TEXT));
            String photoUri = cursor.getString(cursor.getColumnIndex(
                    NoteEntry.COLUMN_NAME_PHOTO_URI));

            NoteModel assessmentNote = new NoteModel();
            assessmentNote.setNoteId(noteId);
            assessmentNote.setType(type);
            assessmentNote.setText(text);
            assessmentNote.setPhotoUri(photoUri);

            assessmentNotes.add(assessmentNote);
        }
        cursor.close();

        Log.d(TAG, "Assessment Notes");
        Log.d(TAG, String.valueOf(assessmentNotes.size()));

        return assessmentNotes;
    }
}
