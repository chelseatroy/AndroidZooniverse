package com.example.chelseatroy.androidzooniverse;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class ZooniverseContentProvider extends ContentProvider {

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private ZooniverseSQLiteOpenHelper mZooniverseSQLiteOpenHelper;

    public static final int PROJECTS = 0;

    static {
        sUriMatcher.addURI("com.example.chelseatroy.androidzooniverse.provider", "projects", PROJECTS);
    }

    @Override
    public boolean onCreate() {
        mZooniverseSQLiteOpenHelper = new ZooniverseSQLiteOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case PROJECTS:
                cursor = mZooniverseSQLiteOpenHelper.getReadableDatabase().query(
                        "projects",
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri newUri;
        switch (sUriMatcher.match(uri)) {
            case PROJECTS:
                long id = mZooniverseSQLiteOpenHelper.getWritableDatabase().insertWithOnConflict(
                        "projects",
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE
                );
                Uri contentUri = Uri.parse("content://com.example.chelseatroy.androidzooniverse.provider/projects");
                newUri = ContentUris.withAppendedId(contentUri, id);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
