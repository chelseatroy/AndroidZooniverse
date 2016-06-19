package com.chelseatroy.androidzooniverse.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ZooniverseSQLiteOpenHelper extends SQLiteOpenHelper {
    public ZooniverseSQLiteOpenHelper(Context context) {
        super(context, "Zooniverse.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE projects (_id INTEGER PRIMARY KEY, title TEXT, description TEXT, slug TEXT, redirect TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
