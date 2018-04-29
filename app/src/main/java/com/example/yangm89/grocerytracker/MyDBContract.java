package com.example.yangm89.grocerytracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by yangm89 on 4/12/2018.
 */

public final class MyDBContract {
    //empty constructor
    public MyDBContract(){};

    //DBEntry class
    public static abstract class DBEntry implements BaseColumns {
        public static final String TABLE_NAME = "userInformation";
        public static final String COLUMN_NAME_USER_ID = "username";
        public static final String COLUMN_NAME_LISTS = "lists";
    }

    //SQLLiteOpenHelper class
    public static class MyDbHelper extends SQLiteOpenHelper {
        //if the schema changes, the version must change
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "GroceryTracker.db";

        public MyDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db){
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {
            //onUpgrade, just start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db,
                                int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = " ,";
    //  private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + DBEntry.TABLE_NAME + " (" +
    //         DBEntry._ID + " INTEGER PRIMARY KEY," + DBEntry.COLUMN_NAME_USER_ID  + TEXT_TYPE + COMMA_SEP +
    //          DBEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE + ")";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + DBEntry.TABLE_NAME + " (" +
            DBEntry.COLUMN_NAME_LISTS + " STRING PRIMARY KEY,"  + TEXT_TYPE + COMMA_SEP +
            DBEntry.COLUMN_NAME_USER_ID + TEXT_TYPE + ")";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBEntry.TABLE_NAME;
}
