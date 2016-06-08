package com.example.chelseatroy.androidzooniverse;

import android.net.Uri;
import android.provider.BaseColumns;

public class ZooniverseContract {
    public static final String AUTHORITY = "com.example.chelseatroy.androidzooniverse.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static class Projects implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZooniverseContract.CONTENT_URI, "projects");
        public static final String TABLE = "projects";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
    }
}
