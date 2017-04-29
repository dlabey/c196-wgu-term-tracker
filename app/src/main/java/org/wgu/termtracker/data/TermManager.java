package org.wgu.termtracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.provider.BaseColumns;

import org.wgu.termtracker.Constants;

import java.util.Date;

public class TermManager extends DBManager implements TermContract {
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
}
