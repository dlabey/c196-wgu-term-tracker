package org.wgu.termtracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.provider.BaseColumns;
import android.util.Log;

import org.apache.commons.lang.StringUtils;
import org.wgu.termtracker.Constants;
import org.wgu.termtracker.models.TermModel;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TermManager extends DBManager implements TermContract {
    private static final String TAG = "TermManager";

    public TermManager(Context context) {
        super(context);
    }

    public long createTerm(String title, Date startDate, Date endDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        String startDateFormatted = simpleDateFormat.format(startDate);
        String endDateFormatted = simpleDateFormat.format(endDate);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TermEntry.COLUMN_NAME_TITLE, title);
        values.put(TermEntry.COLUMN_NAME_START_DATE, startDateFormatted);
        values.put(TermEntry.COLUMN_NAME_END_DATE, endDateFormatted);

        return db.insertOrThrow(TermEntry.TABLE_NAME, null, values);
    }

    public boolean updateTerm(long termId, String title, Date startDate, Date endDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        String startDateFormatted = simpleDateFormat.format(startDate);
        String endDateFormatted = simpleDateFormat.format(endDate);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TermEntry.COLUMN_NAME_TITLE, title);
        values.put(TermEntry.COLUMN_NAME_START_DATE, startDateFormatted);
        values.put(TermEntry.COLUMN_NAME_END_DATE, endDateFormatted);

        String selection = String.format("%s = ?", BaseColumns._ID);
        String[] selectionArgs = { String.valueOf(termId) };

        return db.update(TermEntry.TABLE_NAME, values, selection, selectionArgs) > 0;
    }

    public boolean deleteTerm(long termId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = String.format("%s = ?", BaseColumns._ID);
        String[] selectionArgs = { String.valueOf(termId) };

        return db.delete(TermEntry.TABLE_NAME, selection, selectionArgs) > 0;
    }

    public List<TermModel> listTerms() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = { BaseColumns._ID, TermEntry.COLUMN_NAME_TITLE,
            TermEntry.COLUMN_NAME_START_DATE, TermEntry.COLUMN_NAME_END_DATE };

        String selection = StringUtils.EMPTY;
        String[] selectionArgs = {};

        String sortOrder = "DATE(start_date), DATE(end_date)";

        Cursor cursor = db.query(TermEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);

        List<TermModel> terms = new LinkedList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        while(cursor.moveToNext()) {
            long termId = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndex(TermEntry.COLUMN_NAME_TITLE));
            String startDateStr = cursor
                    .getString(cursor.getColumnIndex(TermEntry.COLUMN_NAME_START_DATE));
            String endDateStr = cursor
                    .getString(cursor.getColumnIndex(TermEntry.COLUMN_NAME_END_DATE));

            Date startDate = null;
            Date endDate = null;
            try {
                startDate = simpleDateFormat.parse(startDateStr);
                endDate = simpleDateFormat.parse(endDateStr);
            } catch (ParseException ex) {
                Log.e(TAG, ex.getMessage());
            }

            TermModel term = new TermModel();
            term.setTermId(termId);
            term.setTitle(title);
            term.setStartDate(startDate);
            term.setEndDate(endDate);

            terms.add(term);
        }
        cursor.close();

        return terms;
    }
}
