package com.example.remainder.evernote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "MyDB";
    private static final String DATABASE_TABLE = "reminders";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "create table reminders (_id integer primary key autoincrement, "
            + "message text not null, date text not null, time text not null);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS reminders");
            onCreate(db);
        }
    }

    // ---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }

    // ---insert a reminder into the database---
    public long insertReminder(String message, String date, String time) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MESSAGE, message);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_TIME, time);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // ---deletes a particular reminder---
    public boolean deleteReminder(String msg) {

        //String whereClause = KEY_CONTENT2 + "= '" + content + "'";

        return db.delete(DATABASE_TABLE, KEY_MESSAGE + " LIKE '" + msg +"'", null) > 0;
    }

    // ---retrieves all the reminders---
    public Cursor getAllReminders() {
        return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_MESSAGE,
                KEY_DATE, KEY_TIME }, null, null, null, null, null);
    }

    // ---retrieves a particular reminder---
    public Cursor getReminder(long rowId) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
                KEY_ROWID, KEY_MESSAGE, KEY_DATE, KEY_TIME }, KEY_ROWID + "="
                + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // ---updates a reminder---
    public long updateReminder(long rowId, String message, String date,
                               String time) {
        ContentValues args = new ContentValues();
        args.put(KEY_MESSAGE, message);
        args.put(KEY_DATE, date);
        args.put(KEY_TIME, time);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null);
    }
}