package com.chelseatroy.androidzooniverse.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ZooniverseSQLiteOpenHelper extends SQLiteOpenHelper {
    public ZooniverseSQLiteOpenHelper(Context context) {
        super(context, "Zooniverse.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ZooniverseContract.Projects.TABLE + " (" +
                ZooniverseContract.Projects._ID + " INTEGER PRIMARY KEY, " +
                ZooniverseContract.Projects.TITLE + " TEXT, " +
                ZooniverseContract.Projects.DESCRIPTION + " TEXT, " +
                ZooniverseContract.Projects.SLUG + " TEXT, " +
                ZooniverseContract.Projects.REDIRECT + " TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion + 1; i <= newVersion; i++) {
            switch (i) {
                case 2:
                    // version 2 changes
                    break;
                case 3:
                    // version 3 changes
                    break;
                case 4:
                    // version 4 changes
                    break;
                case 5:
                    // version 5 changes
                    break;
                // etc.
            }
        }
    }
}
